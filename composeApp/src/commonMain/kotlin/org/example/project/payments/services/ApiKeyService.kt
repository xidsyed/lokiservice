package org.example.project.payments.services

interface ApiKeyService {
    suspend fun fetchApiKeyId(): String
}



