package org.example.project.payments.checkout

import android.app.Activity
import android.content.Context
import com.example.app.TestApiKeyConfig
import com.razorpay.Checkout
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.checkout.model.CheckoutResponse
import org.json.JSONObject
import kotlin.coroutines.resumeWithException

class RazorpayCheckoutProvider(
    private val checkoutActivity: Activity,
    private val applicationContext: Context
) : CheckoutProvider {

    init {
        Checkout.preload(applicationContext)
    }

    private val co = Checkout()

    override suspend fun checkout(
        payload: CheckoutPayload,
    ): CheckoutResponse = suspendCancellableCoroutine { continuation ->
        val jsonString = Json.encodeToString(payload)
        val jsonObject = JSONObject(jsonString)

        val dispatcher = CheckoutNotificationDispatcher
        val listener = object : CheckoutListener {
            override fun onCheckoutNotification(response: CheckoutResponse) {
                continuation.resume(value = response) { _, _, _ -> }
                dispatcher.unregisterListener(this)
            }

            override fun onCheckoutException(e: Exception) {
                continuation.resumeWithException(e)
            }
        }

        dispatcher.registerListener(listener)
        continuation.invokeOnCancellation { dispatcher.unregisterListener(listener) }
        co.setKeyID(TestApiKeyConfig.rzrpayTestKeyId)
        co.open(checkoutActivity, jsonObject)
    }

    override fun clearUserData() {
        Checkout.clearUserData(applicationContext)
    }
}