package org.example.project.loki.geocoder.internal

import org.example.project.loki.geocoder.Geocoder
import org.example.project.loki.geocoder.GeocoderResult
import org.example.project.loki.geocoder.PlatformGeocoder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.example.project.loki.core.Coordinates
import org.example.project.loki.core.Place
import org.example.project.loki.core.exception.NotFoundException
import org.example.project.loki.core.exception.NotSupportedException

internal class DefaultGeocoder(
    override val platformGeocoder: PlatformGeocoder,
    private val dispatcher: CoroutineDispatcher,
) : Geocoder {

    /**
     * @see Geocoder.isAvailable
     */
    override fun isAvailable(): Boolean = platformGeocoder.isAvailable()

    /**
     * @see Geocoder.reverse
     */
    override suspend fun reverse(latitude: Double, longitude: Double): GeocoderResult<Place> {
        return handleResult {
            platformGeocoder.reverse(latitude, longitude)
        }
    }

    /**
     * @see Geocoder.forward
     */
    override suspend fun forward(address: String): GeocoderResult<Coordinates> {
        return handleResult {
            platformGeocoder.forward(address)
        }
    }

    private suspend fun <T> handleResult(block: suspend () -> List<T>): GeocoderResult<T> {
        try {
            if (!isAvailable()) {
                return GeocoderResult.NotSupported
            }
            val place = withContext(dispatcher) { block() }
            if (place.isEmpty()) {
                return GeocoderResult.NotFound
            }

            return GeocoderResult.Success(place)
        } catch (cause: CancellationException) {
            throw cause
        } catch (cause: Throwable) {
            return when (cause) {
                is NotSupportedException -> GeocoderResult.NotSupported
                is IllegalArgumentException -> GeocoderResult.InvalidCoordinates
                is NotFoundException -> GeocoderResult.NotFound
                else -> GeocoderResult.GeocodeFailed(cause.message ?: "Unknown error")
            }
        }
    }
}