package org.example.project.loki.geocoder

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.example.project.loki.geocoder.web.ForwardEndpoint
import org.example.project.loki.geocoder.web.ForwardHttpApiPlatformGeocoder
import org.example.project.loki.core.web.HttpApiEndpoint
import org.example.project.loki.geocoder.web.HttpApiPlatformGeocoder
import org.example.project.loki.geocoder.web.ReverseEndpoint
import org.example.project.loki.geocoder.web.ReverseHttpApiPlatformGeocoder

/**
 * Create a new [Geocoder] instance that uses a [HttpApiPlatformGeocoder].
 *
 * You can create your own [HttpApiPlatformGeocoder] object, or you can use one of the provided
 * extension artifacts.
 *
 * @param platformGeocoder The [HttpApiPlatformGeocoder] to use for geocoding operations.
 * @param dispatcher The [CoroutineDispatcher] to use for geocoding operations.
 * @return A new [Geocoder] instance.
 */
public fun Geocoder(
    platformGeocoder: HttpApiPlatformGeocoder,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geocoder = Geocoder(platformGeocoder as PlatformGeocoder, dispatcher)

/**
 * Create a new [Geocoder] instance that uses a [HttpApiPlatformGeocoder] which is constructed
 * from the supplied [ForwardEndpoint] and [ReverseEndpoint].
 *
 * @param forwardEndpoint The [ForwardEndpoint] to use for forward geocoding operations.
 * @param reverseEndpoint The [ReverseEndpoint] to use for reverse geocoding operations.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param httpClient The [HttpClient] to use for network operations.
 * @param dispatcher The [CoroutineDispatcher] to use for geocoding operations.
 * @return A new [Geocoder] instance.
 */
public fun Geocoder(
    forwardEndpoint: ForwardEndpoint,
    reverseEndpoint: ReverseEndpoint,
    json: Json = HttpApiEndpoint.json(),
    httpClient: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geocoder {
    val apiGeocoder = HttpApiPlatformGeocoder(forwardEndpoint, reverseEndpoint, json, httpClient)
    return Geocoder(apiGeocoder, dispatcher)
}

/**
 * Create a new [Geocoder] instance for forward geocoding operations.
 *
 * @param endpoint The [HttpApiEndpoint] to use for forward geocoding operations.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param httpClient The [HttpClient] to use for network operations.
 * @param dispatcher The [CoroutineDispatcher] to use for geocoding operations.
 * @return A new [Geocoder] instance.
 */
public fun ForwardGeocoder(
    endpoint: ForwardEndpoint,
    json: Json = HttpApiEndpoint.json(),
    httpClient: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): ForwardGeocoder {
    return ForwardGeocoder(ForwardHttpApiPlatformGeocoder(endpoint, httpClient), dispatcher)
}

/**
 * Create a new [Geocoder] instance for reverse geocoding operations.
 *
 * @param endpoint The [HttpApiEndpoint] to use for reverse geocoding operations.
 * @param json The [Json] instance to use for serialization and deserialization.
 * @param httpClient The [HttpClient] to use for network operations.
 * @param dispatcher The [CoroutineDispatcher] to use for geocoding operations.
 * @return A new [Geocoder] instance.
 */
public fun ReverseGeocoder(
    endpoint: ReverseEndpoint,
    json: Json = HttpApiEndpoint.json(),
    httpClient: HttpClient = HttpApiEndpoint.httpClient(json),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): ReverseGeocoder {
    return ReverseGeocoder(ReverseHttpApiPlatformGeocoder(endpoint, httpClient), dispatcher)
}
