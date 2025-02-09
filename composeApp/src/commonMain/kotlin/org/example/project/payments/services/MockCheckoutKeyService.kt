package org.example.project.payments.services

/**
 * A mock implementation of the [CheckoutKeyService] for testing purposes.
 *
 * This class provides a simple, hardcoded way to simulate fetching an API key ID
 * without interacting with any external service. It's primarily used in unit tests
 * to isolate components that depend on [CheckoutKeyService].
 *
 * @property apiKey The hardcoded API key that this service will return.
 */
class MockCheckoutKeyService(
    private val apiKey: String
): CheckoutKeyService {
    override suspend fun fetchApiKeyId(): String {
        return apiKey
    }
}