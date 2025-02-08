package org.example.project.payments.services

import com.example.app.TestApiKeyConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.example.project.Logger
import org.example.project.payments.services.model.Entity
import org.example.project.payments.services.model.OrderRequest
import org.example.project.payments.services.model.PaymentsResponse
import kotlin.concurrent.Volatile

/**
 * `TestRazorpayPaymentService` is a concrete implementation of the `PaymentService` interface
 * that interacts with the Razorpay API for *testing purposes*.
 *
 * This class provides functionalities to:
 *   - Create an order.
 *   - Fetch an order by its ID.
 *   - Fetch payments associated with a specific order ID.
 *
 * It utilizes the Ktor HTTP client for making API requests.
 *
 * The class follows the Singleton pattern via its companion object's `getInstance()` method.
 * This ensures that only one instance of the service exists throughout the application's lifecycle.
 *
 * @property client The Ktor `HttpClient` used for making HTTP requests to the Razorpay API.
 */
class TestRazorpayPaymentService private constructor(
    private val client: HttpClient,
) : PaymentService {
    private val baseUrl = "https://api.razorpay.com/v1"

    override suspend fun createOrder(orderRequest: OrderRequest): Entity {
        val json = Json.encodeToString(orderRequest)
        Logger.d("TAG", "Creating order with request: $orderRequest\nJSON: $json")

        return client.post("$baseUrl/orders") {
            contentType(ContentType.Application.Json)
            setBody(orderRequest)
        }.body()
    }

    override suspend fun fetchOrderById(orderId: String): Entity {
        return client.get("$baseUrl/orders/$orderId").body()
    }

    override suspend fun fetchPaymentsByOrderId(orderId: String): PaymentsResponse {
        return client.get("$baseUrl/orders/$orderId/payments").body()
    }

    companion object {
        @Volatile
        private var instance: TestRazorpayPaymentService? = null

        fun getInstance(
            enableLogging: Boolean = true
        ) = instance ?: TestRazorpayPaymentService(
            client = DefaultHttpClient(
                enableLogging,
                username = TestApiKeyConfig.rzrpayTestKeyId,
                password = TestApiKeyConfig.rzrpayTestKeySecret
            )

        ).also {
            if (instance == null) instance = it
        }
    }
}