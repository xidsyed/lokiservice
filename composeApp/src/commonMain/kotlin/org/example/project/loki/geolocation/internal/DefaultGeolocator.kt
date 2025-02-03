package org.example.project.loki.geolocation.internal

import org.example.project.loki.geolocation.Geolocator
import org.example.project.loki.geolocation.GeolocatorResult
import org.example.project.loki.geolocation.LocationRequest
import org.example.project.loki.geolocation.Locator
import org.example.project.loki.geolocation.TrackingStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.project.loki.core.Location
import org.example.project.loki.core.Priority
import org.example.project.loki.core.exception.NotFoundException
import org.example.project.loki.core.exception.NotSupportedException
import org.example.project.loki.permission.exception.PermissionDeniedException
import org.example.project.loki.permission.exception.PermissionDeniedForeverException
import org.example.project.loki.permission.exception.PermissionException

internal class DefaultGeolocator(
    internal val locator: Locator,
    private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher),
) : Geolocator {

    private val status = MutableStateFlow<TrackingStatus>(TrackingStatus.Idle)
    private var trackingJob: Job? = null

    init {
        coroutineScope.launch {
            locator.locationUpdates.collect { value ->
                status.update { TrackingStatus.Update(value) }
            }
        }
    }

    override val trackingStatus: Flow<TrackingStatus> = status

    override suspend fun isAvailable(): Boolean = locator.isAvailable()

    override suspend fun current(request: LocationRequest): GeolocatorResult {
        return handleResult(request) { locator.current(request.priority) }
    }

    override suspend fun current(priority: Priority): GeolocatorResult =
        current(LocationRequest(priority))

    override fun track(request: LocationRequest): Flow<TrackingStatus> = status.also {
        if (trackingJob?.isActive == true) return@also

        trackingJob = coroutineScope.launch {
            if (!request.ignoreAvailableCheck && !isAvailable()) {
                status.update { TrackingStatus.Error(GeolocatorResult.NotSupported) }
            } else {
                status.update { TrackingStatus.Tracking }

                try {
                    locator.track(request).launchIn(this)
                } catch (cause: Throwable) {
                    if (cause is CancellationException) throw cause
                    status.update { TrackingStatus.Error(cause.toResult()) }
                }
            }
        }
    }

    override fun stopTracking() {
        locator.stopTracking()
        status.update { TrackingStatus.Idle }
        trackingJob?.cancel()
        trackingJob = null
    }

    private fun Throwable.toResult(): GeolocatorResult.Error = when (this) {
        is CancellationException -> throw this
        is PermissionException -> when (this) {
            is PermissionDeniedException -> GeolocatorResult.PermissionDenied(false)
            is PermissionDeniedForeverException -> GeolocatorResult.PermissionDenied(true)
        }
        is NotSupportedException -> GeolocatorResult.NotSupported
        is NotFoundException -> GeolocatorResult.NotFound
        else -> GeolocatorResult.GeolocationFailed(this.message ?: "Unknown error")
    }

    private suspend fun handleResult(
        request: LocationRequest,
        block: suspend () -> Location?,
    ): GeolocatorResult {
        try {
            if (!request.ignoreAvailableCheck && !isAvailable()) {
                return GeolocatorResult.NotSupported
            }
            val result = withContext(dispatcher) { block() }
            if (result == null) {
                return GeolocatorResult.NotFound
            }

            return GeolocatorResult.Success(result)
        } catch (cause: Throwable) {
            return cause.toResult()
        }
    }
}