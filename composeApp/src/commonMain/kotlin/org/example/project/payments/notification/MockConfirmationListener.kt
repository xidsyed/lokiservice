package org.example.project.payments.notification

import kotlinx.coroutines.delay

class MockConfirmationListener(
    private val expectedResponse: ConfirmationResponse
) : ConfirmationListener {
    override suspend fun getConfirmation(): ConfirmationResponse {
        delay(3000)
        return expectedResponse
    }
}

