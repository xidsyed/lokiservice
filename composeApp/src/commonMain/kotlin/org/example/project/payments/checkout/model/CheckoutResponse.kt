package org.example.project.payments.checkout.model

sealed class CheckoutResponse {
    data class Error(
        val errorCode: ErrorCode,
        val response: String? = null,
        val checkoutResult: CheckoutResult? = null
    ) : CheckoutResponse() {
        enum class ErrorCode {
            CHECKOUT_INVALID_OPTIONS,
            CHECKOUT_CANCELLED,
            CHECKOUT_TLS_ERROR,
            CHECKOUT_NETWORK_ERROR
        }
    }

    data class Success(
        val result: CheckoutResult
    ) : CheckoutResponse()
}

