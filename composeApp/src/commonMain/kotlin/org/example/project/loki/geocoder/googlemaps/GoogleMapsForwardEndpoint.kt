package org.example.project.loki.geocoder.googlemaps

import org.example.project.loki.geocoder.googlemaps.google.internal.GeocodeResponse
import org.example.project.loki.geocoder.googlemaps.google.internal.resultsOrThrow
import org.example.project.loki.geocoder.googlemaps.google.internal.toCoordinates
import org.example.project.loki.geocoder.googlemaps.parameter.GoogleMapsParameters
import org.example.project.loki.geocoder.googlemaps.parameter.GoogleMapsParametersBuilder
import org.example.project.loki.geocoder.googlemaps.parameter.googleMapsParameters
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodeURLQueryComponent
import org.example.project.loki.core.Coordinates
import org.example.project.loki.core.InternalLokiApi
import org.example.project.loki.geocoder.web.ForwardEndpoint

/**
 * A [ForwardEndpoint] that uses the Google Maps Geocoding API.
 *
 * @property apiKey The API key to use for the Google Maps Geocoding API.
 * @property parameters The parameters to use for the Google Maps Geocoding API.
 */
public class GoogleMapsForwardEndpoint(
    private val apiKey: String,
    private val parameters: GoogleMapsParameters = GoogleMapsParameters(),
) : ForwardEndpoint {

    /**
     * Creates a new [GoogleMapsForwardEndpoint] with the given API key.
     *
     * @param apiKey The API key to use for the Google Maps Geocoding API.
     * @param block A block to configure the parameters for the Google Maps Geocoding API.
     */
    public constructor(
        apiKey: String,
        block: GoogleMapsParametersBuilder.() -> Unit,
    ) : this(apiKey, googleMapsParameters(block))

    override fun url(param: String): String {
        val encodedQuery = param.encodeURLQueryComponent()
        return GoogleMapsPlatformGeocoder.forwardUrl(encodedQuery, apiKey, parameters)
    }

    @OptIn(InternalLokiApi::class)
    override suspend fun mapResponse(response: HttpResponse): List<Coordinates> {
        val result = response.body<GeocodeResponse>().resultsOrThrow()
        return result.toCoordinates()
    }
}