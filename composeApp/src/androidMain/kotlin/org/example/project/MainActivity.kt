package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.example.project.payments.checkout.CheckoutNotificationDispatcher
import org.example.project.payments.checkout.internal.toCheckoutErrorCode
import org.example.project.payments.checkout.model.CheckoutResponse
import org.example.project.payments.checkout.model.CheckoutResult
import org.example.project.presentation.App

class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)

        setContent {
            App()

        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData) {
        try {
            val checkoutResponse = CheckoutResponse.Success(
                result = CheckoutResult(
                    paymentId = razorpayPaymentId,
                    orderId = paymentData.orderId,
                    platformSignature = paymentData.signature
                )
            )
            CheckoutNotificationDispatcher.notifyResponse(checkoutResponse)
        }catch (e: Exception) {
            CheckoutNotificationDispatcher.throwException(e)
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?, paymentData: PaymentData) {
        try {
            val checkoutError = CheckoutResponse.Error(
                errorCode = errorCode.toCheckoutErrorCode(),
                response = response,
            )
            CheckoutNotificationDispatcher.notifyResponse(checkoutError)
            Logger.d("TAG", "called")
        } catch (e: Exception) {
            CheckoutNotificationDispatcher.throwException(e)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}