package org.example.project.payments.services.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentsResponse (
    @SerialName("entity") val entity :String,
    @SerialName("count") val count : Int,
    @SerialName("items") val items : List<Entity>
)