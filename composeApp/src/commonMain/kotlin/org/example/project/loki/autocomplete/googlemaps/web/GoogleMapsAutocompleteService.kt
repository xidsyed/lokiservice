@file:Suppress("FunctionName")

package org.example.project.loki.autocomplete.googlemaps.web

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.serialization.json.Json
import org.example.project.loki.autocomplete.AutocompletePlace
import org.example.project.loki.autocomplete.AutocompleteService
import org.example.project.loki.autocomplete.googlemaps.GoogleMapsAutocompleteEndpoint
import org.example.project.loki.core.InternalCompassApi
import org.example.project.loki.core.Place
import org.example.project.loki.core.web.HttpApiEndpoint
import org.example.project.loki.core.web.makeRequest
import org.example.project.loki.geocoder.googlemaps.google.internal.GeocodeResponse
import org.example.project.loki.geocoder.googlemaps.google.internal.resultsOrThrow
import org.example.project.loki.geocoder.googlemaps.google.internal.toPlaces

/**
 * Creates a new [AutocompleteService] that uses the Google Maps Autocomplete API to provide
 * autocomplete suggestions.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use when making requests to the Google Maps Geocoding API.
 * @param json The JSON serializer to use when parsing responses.
 * @param client The HTTP client to use when making requests.
 * @return A new [AutocompleteService] that uses the Google Maps Geocoding API.
 */
@OptIn(InternalCompassApi::class)
public fun GoogleMapsAutocompleteService(
    apiKey: String,
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json),
): AutocompleteService<AutocompletePlace> {
    // AutoComplete EndPoint
    val endpoint = GoogleMapsAutocompleteEndpoint(apiKey)

    return object : AutocompleteService<AutocompletePlace> {
        override fun isAvailable(): Boolean = apiKey.isNotBlank()

        override suspend fun search(query: String): List<AutocompletePlace> {
            val url = endpoint.url(query)
            return client.makeRequest(url, endpoint::mapResponse)
        }
    }
}

/**
 * Creates a new [AutocompleteService] that uses the Google Maps Geocoding API to provide
 * autocomplete suggestions.
 *
 * @param apiKey The Google Maps API key.
 * @param parameters The parameters to use when making requests to the Google Maps Geocoding API.
 * @param json The JSON serializer to use when parsing responses.
 * @param client The HTTP client to use when making requests.
 * @return A new [AutocompleteService] that uses the Google Maps Geocoding API.
 */
public fun AutocompleteService.Companion.googleMapsAutoComplete(
    apiKey: String,
    enableLogging : Boolean = false,
    json: Json = HttpApiEndpoint.json(),
    client: HttpClient = HttpApiEndpoint.httpClient(json, enableLogging),
): AutocompleteService<AutocompletePlace> =
    GoogleMapsAutocompleteService(apiKey, json, client)


