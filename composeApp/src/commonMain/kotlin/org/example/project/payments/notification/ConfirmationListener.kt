package org.example.project.payments.notification

interface ConfirmationListener {
    suspend fun getConfirmation(): ConfirmationResponse
}

sealed class ConfirmationResponse {
    data class Error(val errorCode: ErrorCode, val message: String) : ConfirmationResponse() {
        enum class ErrorCode {
            NETWORK_ERROR,
            CONFIRMATION_FAILED,
            TIME_OUT
        }
    }
    data class Success(val success: String): ConfirmationResponse()
}