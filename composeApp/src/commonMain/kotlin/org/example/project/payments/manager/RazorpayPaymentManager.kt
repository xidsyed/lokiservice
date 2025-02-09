package org.example.project.payments.manager

import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.example.project.Logger
import org.example.project.payments.checkout.CheckoutProvider
import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.checkout.model.CheckoutResponse
import org.example.project.payments.manager.model.PaymentException
import org.example.project.payments.manager.model.PaymentStatus
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.API_KEY_FETCH_ERROR
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.CHECKOUT_PROVIDER_ERROR
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.ILLEGAL_ARGUMENT_EXCEPTION
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.ORDER_CREATION_ERROR
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.UNKNOWN
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.WEB_EXCEPTION
import org.example.project.payments.manager.model.toPaymentError
import org.example.project.payments.notification.ConfirmationListener
import org.example.project.payments.notification.ConfirmationResponse
import org.example.project.payments.services.CheckoutKeyService
import org.example.project.payments.services.PaymentService
import org.example.project.payments.services.model.OrderRequest


class RazorpayPaymentManager(
    private val scope: CoroutineScope,
    private val confirmationListener: ConfirmationListener,
    private val paymentService: PaymentService,
    private val apiKeyService: CheckoutKeyService,
    private val checkoutProvider: CheckoutProvider,
): PaymentManager {

    private val statusChannel =
        Channel<PaymentStatus>(
            capacity = 3,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
            onUndeliveredElement = {
                Logger.d("TAG", "Payment Status Undelivered : $it")
            },
        )
    override val statusFlow = statusChannel.consumeAsFlow()

    private var mutex = Mutex(false)

    /**
     * Initiates the payment checkout process.
     *
     * This function handles the entire checkout flow, including payload validation,
     * initialization, and error handling. It ensures that only one checkout operation
     * can be active at a time using a mutex.
     *
     * The function updates a [Flow] of [PaymentStatus] to reflect the current state of
     * the checkout process. Consumers can subscribe to this flow to receive updates
     * about the checkout's progress or any errors that occur.
     *
     * @param payload The [CheckoutPayload] containing the details of the checkout.
     * @return A [Flow] of [PaymentStatus] that emits updates on the checkout's status.
     *         The possible states are:
     *         - [PaymentStatus.IDLE]: The initial and final state, indicating no active checkout.
     *         - [PaymentStatus.Initialized]: Indicates the checkout process has been initialized with the payload.
     *         - [PaymentStatus.PaymentError]: Indicates an error occurred during the checkout process.
     *           - */
    override fun checkout(payload: CheckoutPayload): Flow<PaymentStatus> {
        scope.launch(Dispatchers.IO) {
            mutex.lock(this)
            statusChannel.send(PaymentStatus.Initialized(payload))
            try {
                validatePayload(payload)
                initializeCheckout(payload)
            } catch (e: Exception) {
                val paymentError = when (e) {
                    is IllegalArgumentException -> PaymentStatus.PaymentError(
                        issue = ILLEGAL_ARGUMENT_EXCEPTION,
                        message = e.message
                    )

                    is PaymentException -> PaymentStatus.PaymentError(
                        issue = e.issue,
                        message = e.throwable.message
                    )

                    is ClientRequestException -> PaymentStatus.PaymentError(
                        issue = WEB_EXCEPTION,
                        message = "${e.response.status.value} : ${e.message}"
                    )

                    else -> {
                        e.printStackTrace()
                        PaymentStatus.PaymentError(
                            issue = UNKNOWN,
                            message = "Unknown Error : ${e.message}"
                        )
                    }
                }
                statusChannel.send(paymentError)


            } finally {
                mutex.unlock()
                statusChannel.send(PaymentStatus.IDLE)

            }
        }
        return statusFlow
    }

    private suspend fun initializeCheckout(payload: CheckoutPayload) {
        val orderRequest = OrderRequest(
            amount = payload.amount,
            currency = payload.currency,
        )

        val orderId = runCatching { paymentService.createOrder(orderRequest) }.getOrElse {
            throw PaymentException(throwable = it, issue = ORDER_CREATION_ERROR)
        }

        val apiKeyId = runCatching { apiKeyService.fetchApiKeyId() }.getOrElse {
            throw PaymentException(throwable = it, issue = API_KEY_FETCH_ERROR)
        }

        val finalPayload = payload.copy(orderId = orderId.id, key = apiKeyId)

        val checkoutResponse = runCatching { checkoutProvider.checkout(finalPayload) }.getOrElse {
            throw PaymentException(
                throwable = it,
                issue = CHECKOUT_PROVIDER_ERROR,
            )
        }

        when (checkoutResponse) {
            is CheckoutResponse.Success -> statusChannel.send(
                PaymentStatus.CheckoutSuccessful(
                    checkoutResponse
                )
            )

            is CheckoutResponse.Error -> {
                statusChannel.send(checkoutResponse.toPaymentError())
                return
            }
        }

        val confirmationResult = confirmationListener.getConfirmation()
        statusChannel.send(
            when (confirmationResult) {
                is ConfirmationResponse.Success -> PaymentStatus.ConfirmationSuccess(data = confirmationResult)
                is ConfirmationResponse.Error -> confirmationResult.toPaymentError()
            }
        )
    }

    override fun validatePayload(payload: CheckoutPayload) {
        if (payload.name.isBlank() || payload.currency.isBlank()) {
            throw IllegalArgumentException("Name and currency cannot be blank")
        }
        if (payload.amount < 100) {
            throw IllegalArgumentException("Amount should be greater than zero")
        }
    }

    override fun clearUserData() {
        checkoutProvider.clearUserData()
    }

    companion object
}






