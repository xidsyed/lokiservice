package org.example.project.payments.checkout

import org.example.project.payments.checkout.model.CheckoutResponse
import kotlin.concurrent.Volatile


/*
object CheckoutObserver {
    private val listeners = mutableListOf<CheckoutListener>()

    internal fun onCheckoutSuccess(paymentId: String?, paymentData: PaymentData) {
        val checkoutResult = CheckoutResult(
            paymentId = paymentId,
            orderId = paymentData.orderId,
            platformSignature = paymentData.signature
        )
        listeners.forEach { observer ->
            try {
                observer.onCheckoutSuccess(checkoutResult)
            } catch (e: Exception) {
                Log.e("CheckoutObserver", "Error in onCheckoutSuccess", e)
            }
        }
    }

    internal fun onCheckoutFailure(errorCode: Int, response: String?, paymentData: PaymentData) {
        val checkoutError = CheckoutError(
            errorCode = errorCode.toCheckoutErrorCode(),
            response = response,
            checkoutResult = CheckoutResult(
                paymentId = paymentData.paymentId,
                orderId = paymentData.orderId,
                platformSignature = paymentData.signature
            )
        )
        listeners.forEach { observer ->
            observer.onCheckoutError(checkoutError)
        }
    }

    fun registerListener(listener: CheckoutListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: CheckoutListener) {
        listeners.remove(listener)
    }
}

*/

object CheckoutNotificationDispatcher {
    @Volatile
    private var listener: CheckoutListener? = null

    fun registerListener(listener: CheckoutListener) {
        this.listener = listener
    }

    fun unregisterListener(listener: CheckoutListener) {
        if (this.listener == listener) this.listener = null
    }

    internal fun notifyResponse(response: CheckoutResponse) {
        listener?.onCheckoutNotification(response)
            ?: throw IllegalStateException("CheckoutListener is not available")
    }

    internal fun throwException(e: Exception) {
        listener?.onCheckoutException(e)
            ?: throw IllegalStateException("CheckoutListener is not available")
    }
}


