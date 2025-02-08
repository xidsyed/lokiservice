package org.example.project.payments.checkout

import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.checkout.model.CheckoutResponse

interface CheckoutProvider {
    suspend fun checkout(payload: CheckoutPayload) : CheckoutResponse
    fun clearUserData()
}

expect fun CheckoutProvider() : CheckoutProvider

