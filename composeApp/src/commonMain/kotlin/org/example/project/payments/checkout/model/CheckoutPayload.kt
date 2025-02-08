package org.example.project.payments.checkout.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CheckoutPayload(
    // Mandatory fields
    @SerialName("key")
    val key: String,

    @SerialName("amount")
    val amount: Int,

    @SerialName("currency")
    val currency: String,

    @SerialName("name")
    val name: String,

    @SerialName("order_id")
    val orderId: String,

    // Optional fields
    @SerialName("description")
    val description: String? = null,

    @SerialName("image")
    val image: String? = null,

    @SerialName("prefill")
    val prefill: Prefill? = null,

    @SerialName("notes")
    val notes: Notes? = null,

    @SerialName("theme")
    val theme: Theme? = null,

    @SerialName("modal")
    val modal: Modal? = null,

    @SerialName("subscription_id")
    val subscriptionId: String? = null,

    @SerialName("subscription_card_change")
    val subscriptionCardChange: Boolean? = null,

    @SerialName("recurring")
    val recurring: Boolean? = null,

    @SerialName("callback_url")
    val callbackUrl: String? = null,

    @SerialName("redirect")
    val redirect: Boolean? = null,

    @SerialName("customer_id")
    val customerId: String? = null,

    @SerialName("remember_customer")
    val rememberCustomer: Boolean? = null,

    @SerialName("timeout")
    val timeout: Int? = null,

    @SerialName("readonly")
    val readonly: Readonly? = null,

    @SerialName("hidden")
    val hidden: Hidden? = null,

    @SerialName("send_sms_hash")
    val sendSmsHash: Boolean? = null,

    @SerialName("allow_rotation")
    val allowRotation: Boolean? = null,

    @SerialName("retry")
    val retry: Retry? = null,

    @SerialName("config")
    val config: Config? = null
) {
    companion object {
        fun defaultPayload(
            amount: Int,
            name: String,
            customerName: String,
            customerEmail: String,
            customerPhoneNumber: String,
            defaultMethod : Method? = null
        ) = CheckoutPayload(
            amount = amount,
            currency = "INR",
            name = name,
            key = "",
            orderId = "",
            prefill = Prefill(
                name = customerName,
                email = customerEmail,
                contact = customerPhoneNumber,
                method = defaultMethod,
            ),
            theme = Theme(
                hideTopBar = true,
                color = "#ffffff",
                backdropColor = "#ffffff"
            ),
            hidden = Hidden(
                email = true,
                contact = true
            ),
            readonly = Readonly(
                contact = true,
                email = true,
                name = true
            )
        )
    }
}


@Serializable
data class Prefill(
    @SerialName("name")
    val name: String,

    @SerialName("email")
    val email: String,

    @SerialName("contact")
    val contact: String,

    // Optional pre-selected payment method
    @SerialName("method")
    val method: Method? = null
)

enum class Method {
    @SerialName("card")
    CARD,
    @SerialName("netbanking")
    NET_BANKING,
    @SerialName("wallet")
    WALLET,
    @SerialName("emi")
    EMI,
    @SerialName("upi")
    UPI
}


// Assuming the keys in notes are known in advance.
// If they are dynamic, consider using a Map<String, String> instead.
@Serializable
data class Notes(
    @SerialName("object")
    val jsonObject: JsonObject? = null,
)

@Serializable
data class Theme(
    @SerialName("hide_topbar")
    val hideTopBar: Boolean,

    @SerialName("color")
    val color: String,

    @SerialName("backdrop_color")
    val backdropColor: String
)

@Serializable
data class Modal(
    @SerialName("backdropclose")
    val backdropClose: Boolean,

    @SerialName("escape")
    val escape: Boolean,

    @SerialName("handleback")
    val handleBack: Boolean,

    @SerialName("confirm_close")
    val confirmClose: Boolean,

    @SerialName("animation")
    val animation: Boolean,

    // This field represents a function callback in JS. Here, it's represented as a String placeholder.
    @SerialName("ondismiss")
    val onDismiss: String
)

@Serializable
data class Readonly(
    @SerialName("contact")
    val contact: Boolean,

    @SerialName("email")
    val email: Boolean,

    @SerialName("name")
    val name: Boolean
)

@Serializable
data class Hidden(
    @SerialName("contact")
    val contact: Boolean,

    @SerialName("email")
    val email: Boolean
)

@Serializable
data class Retry(
    @SerialName("enabled")
    val enabled: Boolean,

    @SerialName("max_count")
    val maxCount: Int
)

@Serializable
data class Config(
    @SerialName("display")
    val display: Display
)

@Serializable
data class Display(
    @SerialName("language")
    val language: String
)
