package org.example.project.payments.services

interface CheckoutKeyService {
    suspend fun fetchApiKeyId(): String
}



