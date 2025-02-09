package org.example.project.payments.manager

import kotlinx.coroutines.flow.Flow
import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.manager.model.PaymentStatus

interface PaymentManager {
    val statusFlow: Flow<PaymentStatus>
    fun checkout(payload: CheckoutPayload): Flow<PaymentStatus>
    fun validatePayload(payload: CheckoutPayload)
    fun clearUserData(): Unit
    companion object
}