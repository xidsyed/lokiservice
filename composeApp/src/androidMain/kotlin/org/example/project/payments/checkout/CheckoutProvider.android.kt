package org.example.project.payments.checkout

import org.example.project.payments.checkout.internal.CheckoutActivityProvider
import org.example.project.payments.checkout.internal.context.ContextProvider

actual fun CheckoutProvider(): CheckoutProvider {
    val context = ContextProvider.getInstance().context
    val checkoutActivity = CheckoutActivityProvider.getCheckoutActivity()

    return RazorpayCheckoutProvider(
        checkoutActivity = checkoutActivity,
        applicationContext = context.applicationContext
    )
}