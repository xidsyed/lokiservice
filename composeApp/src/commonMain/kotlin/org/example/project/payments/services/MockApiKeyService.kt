package org.example.project.payments.services

import com.example.app.TestApiKeyConfig

class MockApiKeyService(private val overrideKey: String? = null): ApiKeyService {
    override suspend fun fetchApiKeyId(): String {
        return overrideKey ?: TestApiKeyConfig.rzrpayTestKeyId
    }

}