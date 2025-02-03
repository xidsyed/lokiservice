package org.example.project.loki.autocomplete

import org.example.project.loki.autocomplete.googlemaps.web.internal.PlaceAutocompletePrediction


data class AutocompletePlace(
    val placeId: String,
    val address: String
)

fun PlaceAutocompletePrediction.toAutoCompletePlace() : AutocompletePlace {
    return AutocompletePlace(
        placeId = placeId,
        address = description
    )
}