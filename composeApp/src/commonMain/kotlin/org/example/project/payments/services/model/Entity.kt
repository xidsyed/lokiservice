package org.example.project.payments.services.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Entity(
    @SerialName("amount") val amount: Int,
    @SerialName("amount_due") val amountDue: Int,
    @SerialName("amount_paid") val amountPaid: Int,
    @SerialName("attempts") val attempts: Int,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("currency") val currency: String,
    @SerialName("entity") val entity: String,
    @SerialName("id") val id: String,
    @SerialName("notes") val notes: List<String>,
    @SerialName("offer_id") val offerId: String? = null,
    @SerialName("receipt") val receipt: String? = null,
    @SerialName("status") val status: String
)
