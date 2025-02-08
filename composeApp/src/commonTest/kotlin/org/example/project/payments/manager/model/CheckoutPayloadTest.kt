package org.example.project.payments.manager.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.checkout.model.Config
import org.example.project.payments.checkout.model.Display
import org.example.project.payments.checkout.model.Hidden
import org.example.project.payments.checkout.model.Method
import org.example.project.payments.checkout.model.Modal
import org.example.project.payments.checkout.model.Notes
import org.example.project.payments.checkout.model.Prefill
import org.example.project.payments.checkout.model.Readonly
import org.example.project.payments.checkout.model.Retry
import org.example.project.payments.checkout.model.Theme
import kotlin.test.Test
import kotlin.test.assertTrue

val checkoutForm = CheckoutPayload(
    key = "your_api_key_here",
    amount = 29900,
    currency = "INR",
    name = "Acme Corp",
    orderId = "order_DBJOWzybf0sJbb",
    description = "Purchase description goes here",
    image = "https://example.com/logo.png",
    prefill = Prefill(
        name = "Gaurav Kumar",
        email = "gaurav.kumar@example.com",
        contact = "+919977665544",
        method = Method.CARD
    ),
    notes = Notes(
        jsonObject = buildJsonObject {
            put("key", "value")
        }
    ),
    theme = Theme(
        hideTopBar = false,
        color = "#F37254",
        backdropColor = "#000000"
    ),
    modal = Modal(
        backdropClose = false,
        escape = true,
        handleBack = true,
        confirmClose = false,
        animation = true,
        onDismiss = " "
    ),
    subscriptionId = "sub_DBJOWzybf0sJbb",
    subscriptionCardChange = false,
    recurring = false,
    callbackUrl = "https://example.com/payment_callback",
    redirect = false,
    customerId = "cust_DBJOWzybf0sJbb",
    rememberCustomer = false,
    timeout = 300,
    readonly = Readonly(
        contact = false,
        email = false,
        name = false
    ),
    hidden = Hidden(
        contact = false,
        email = false
    ),
    sendSmsHash = false,
    allowRotation = false,
    retry = Retry(
        enabled = true,
        maxCount = 4
    ),
    config = Config(
        display = Display(
            language = "en"
        )
    )
)


class CheckoutPayloadTest {

    @Test
    fun encodingTest() {
        val json = Json { prettyPrint = true }
        val jsonString = json.encodeToString(checkoutForm)

        val expectedJsonString = """
            {
                "key": "your_api_key_here",
                "amount": 29900,
                "currency": "INR",
                "name": "Acme Corp",
                "order_id": "order_DBJOWzybf0sJbb",
                "description": "Purchase description goes here",
                "image": "https://example.com/logo.png",
                "prefill": {
                    "name": "Gaurav Kumar",
                    "email": "gaurav.kumar@example.com",
                    "contact": "+919977665544",
                    "method": "card"
                },
                "notes": {
                    "key": "value",
                },
                "theme": {
                    "hide_topbar": false,
                    "color": "#F37254",
                    "backdrop_color": "#000000"
                },
                "modal": {
                    "backdropclose": false,
                    "escape": true,
                    "handleback": true,
                    "confirm_close": false,
                    "animation": true,
                    "ondismiss": " "
                },
                "subscription_id": "sub_DBJOWzybf0sJbb",
                "subscription_card_change": false,
                "recurring": false,
                "callback_url": "https://example.com/payment_callback",
                "redirect": false,
                "customer_id": "cust_DBJOWzybf0sJbb",
                "remember_customer": false,
                "timeout": 300,
                "readonly": {
                    "contact": false,
                    "email": false,
                    "name": false
                },
                "hidden": {
                    "contact": false,
                    "email": false
                },
                "send_sms_hash": false,
                "allow_rotation": false,
                "retry": {
                    "enabled": true,
                    "max_count": 4
                },
                "config": {
                    "display": {
                        "language": "en"
                    }
                }
            }
        """.trimIndent()
        assertTrue { jsonString == expectedJsonString }
    }
}
