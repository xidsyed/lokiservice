package org.example.project.loki.geocoder.googlemaps.google.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.project.loki.core.InternalLokiApi

@InternalLokiApi
@Serializable
public data class GeometryResponse(
    @Serializable
    val location: LocationResponse? = null,
)

@InternalLokiApi
@Serializable
public data class LocationResponse(
    @SerialName("lat")
    val lat: Double,

    @SerialName("lng")
    val lng: Double,
)