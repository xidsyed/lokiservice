package org.example.project.payments.checkout.model

data class CheckoutResult(
    val paymentId: String?,
    val orderId: String?,
    val platformSignature: String
)
