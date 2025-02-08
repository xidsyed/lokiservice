package org.example.project.payments.manager.model

import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.checkout.model.CheckoutResponse
import org.example.project.payments.checkout.model.CheckoutResponse.Error.ErrorCode
import org.example.project.payments.manager.model.PaymentStatus.PaymentError
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.BAD_REQUEST
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.CONFIRMATION_FAILED
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.CONFIRMATION_TIMED_OUT
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.NETWORK_ERROR
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.CHECKOUT_CANCELLED
import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue.TLS_ERROR
import org.example.project.payments.notification.ConfirmationResponse
import org.example.project.payments.notification.ConfirmationResponse.Error.ErrorCode as ConfirmationErrorCode


sealed class PaymentStatus {
    data object IDLE : PaymentStatus()

    class Initialized(val payload: CheckoutPayload) : PaymentStatus()


    class CheckoutSuccessful(val data: CheckoutResponse.Success) : PaymentStatus()
    class ConfirmationSuccess(val data: ConfirmationResponse.Success) : PaymentStatus()
    data class PaymentError(val issue: Issue, val message: String?) :
        PaymentStatus() {
        enum class Issue(val value: String) {
            NETWORK_ERROR("Network Error"),
            ORDER_CREATION_ERROR("Couldn't Fetch Payment Order"),
            API_KEY_FETCH_ERROR("Couldn't Fetch API Key"),
            CHECKOUT_PROVIDER_ERROR("Checkout Provider Error"),
            BAD_REQUEST("Bad Request"),
            ILLEGAL_ARGUMENT_EXCEPTION("Illegal Arguments Provided"),
            CHECKOUT_CANCELLED("Checkout Cancelled"),
            TLS_ERROR("TLS Error"),
            CONFIRMATION_FAILED("Payment Confirmation Failed"),
            CONFIRMATION_TIMED_OUT("Payment Confirmation Timed Out"),
            WEB_EXCEPTION("Web Exception"),
            UNKNOWN("Unknown Error")
        }

        override fun toString(): String {
            return "${issue.value} : $message"
        }
    }


}


fun CheckoutResponse.Error.toPaymentError(): PaymentError {
    return when (this.errorCode) {
        ErrorCode.CHECKOUT_INVALID_OPTIONS -> PaymentError(BAD_REQUEST, this.response)
        ErrorCode.CHECKOUT_CANCELLED -> PaymentError(CHECKOUT_CANCELLED, this.response)
        ErrorCode.CHECKOUT_TLS_ERROR -> PaymentError(TLS_ERROR, this.response)
        ErrorCode.CHECKOUT_NETWORK_ERROR -> PaymentError(NETWORK_ERROR, this.response)
    }
}

fun ConfirmationResponse.Error.toPaymentError(): PaymentError {
    return when (this.errorCode) {
        ConfirmationErrorCode.NETWORK_ERROR -> PaymentError(
            issue = NETWORK_ERROR,
            message = "Confirmation Failed : Network Error"
        )

        ConfirmationErrorCode.CONFIRMATION_FAILED -> PaymentError(
            issue = CONFIRMATION_FAILED,
            message = "Confirmation Failed : Authorization Failed"
        )

        ConfirmationErrorCode.TIME_OUT -> PaymentError(
            issue = CONFIRMATION_TIMED_OUT,
            message = "Confirmation Failed : Request Timed Out"
        )

    }
}