package org.example.project.payments.checkout.internal

import com.razorpay.Checkout
import org.example.project.payments.checkout.model.CheckoutResponse.Error.ErrorCode

fun Int.toCheckoutErrorCode(): ErrorCode {
    return when (this) {
        Checkout.INVALID_OPTIONS -> ErrorCode.CHECKOUT_INVALID_OPTIONS
        Checkout.PAYMENT_CANCELED -> ErrorCode.CHECKOUT_CANCELLED
        Checkout.TLS_ERROR -> ErrorCode.CHECKOUT_TLS_ERROR
        else -> ErrorCode.CHECKOUT_NETWORK_ERROR
    }
}