package org.example.project.payments.manager

import kotlinx.coroutines.CoroutineScope
import org.example.project.payments.checkout.CheckoutProvider
import org.example.project.payments.notification.ConfirmationResponse
import org.example.project.payments.notification.MockConfirmationListener
import org.example.project.payments.services.MockApiKeyService
import org.example.project.payments.services.TestRazorpayPaymentService

fun PaymentManager.Companion.createTest(
    scope: CoroutineScope
): PaymentManager {
    return PaymentManager(
        scope = scope,
        confirmationListener = MockConfirmationListener(
            expectedResponse = ConfirmationResponse.Success("Successful")
        ),
        paymentService = TestRazorpayPaymentService.getInstance(),
        apiKeyService = MockApiKeyService(),
        checkoutProvider = CheckoutProvider()
    )
}