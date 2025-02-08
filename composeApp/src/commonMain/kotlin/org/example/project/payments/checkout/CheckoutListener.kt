package org.example.project.payments.checkout

import org.example.project.payments.checkout.model.CheckoutResponse

interface CheckoutListener {
    fun onCheckoutNotification(response: CheckoutResponse)
    fun onCheckoutException(e: Exception)
}

