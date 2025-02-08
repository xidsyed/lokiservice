package org.example.project.payments.services

import org.example.project.payments.services.model.OrderRequest
import org.example.project.payments.services.model.Entity
import org.example.project.payments.services.model.PaymentsResponse


/**
 * - Interface defining the contract for payment-related operations.
 * - This service provides functionalities to create, fetch, and manage orders and their associated payments.
 * */
interface PaymentService {
    suspend fun createOrder(orderRequest: OrderRequest): Entity
    suspend fun fetchOrderById(orderId: String): Entity
    suspend fun fetchPaymentsByOrderId(orderId: String): PaymentsResponse
}


