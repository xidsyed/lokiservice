package org.example.project.payments.services.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    @SerialName("amount") val amount: Int,
    @SerialName("currency") val currency: String,
    @SerialName("receipt") val receipt: String? = null,
)