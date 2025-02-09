package org.example.project.payments.manager

import kotlinx.coroutines.CoroutineScope
import org.example.project.payments.checkout.RazorpayCheckoutProvider
import org.example.project.payments.notification.ConfirmationResponse
import org.example.project.payments.notification.MockConfirmationListener
import org.example.project.payments.services.MockCheckoutKeyService
import org.example.project.payments.services.TestRazorpayPaymentService

fun PaymentManager.Companion.razorpayTest(
    scope: CoroutineScope,
    testKeyId: String,
    testKeySecret: String,
): RazorpayPaymentManager {
    return RazorpayPaymentManager(
        scope = scope,
        confirmationListener = MockConfirmationListener(
            expectedResponse = ConfirmationResponse.Success("Successful")
        ),
        paymentService = TestRazorpayPaymentService.getInstance(
            enableLogging = true,
            testKeyId = testKeyId,
            testKeySecret = testKeySecret
        ),
        apiKeyService = MockCheckoutKeyService(testKeyId),
        checkoutProvider = RazorpayCheckoutProvider()
    )
}

fun PaymentManager.Companion.razorpayStaging(
    scope: CoroutineScope,
    stagingApiToken: String
): RazorpayPaymentManager {
    TODO("Not yet implemented")
}

fun PaymentManager.Companion.razorpayProduction(
    scope: CoroutineScope,
    productionApiToken: String
): RazorpayPaymentManager {
    TODO("Not yet implemented")
}