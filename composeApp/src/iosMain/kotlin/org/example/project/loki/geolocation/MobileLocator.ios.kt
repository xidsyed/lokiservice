package org.example.project.loki.geolocation

import org.example.project.loki.geolocation.internal.toModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.example.project.loki.core.Location
import org.example.project.loki.core.Priority
import org.example.project.loki.geolocation.exception.GeolocationException
import org.example.project.loki.geolocation.internal.LocationManagerDelegate
import org.example.project.loki.geolocation.internal.toIosPriority
import org.example.project.loki.permission.LocationPermissionController
import org.example.project.loki.permission.PermissionState
import org.example.project.loki.permission.throwOnError
import platform.CoreLocation.CLLocationManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal actual fun createLocator(
    permissionController: LocationPermissionController,
): MobileLocator {
    return IosLocator(permissionController)
}

internal class IosLocator(
    private val permissionController: LocationPermissionController,
    private val locationDelegate: LocationManagerDelegate = LocationManagerDelegate(),
) : MobileLocator {

    private val _locationUpdates = MutableSharedFlow<Location>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val locationUpdates: Flow<Location> = _locationUpdates

    init {
        locationDelegate.monitorLocation { location ->
            _locationUpdates.tryEmit(location.toModel())
        }
    }

    override suspend fun isAvailable(): Boolean {
        return CLLocationManager.locationServicesEnabled()
    }

    override fun hasPermission(): Boolean {
        return permissionController.hasPermission()
    }

    override suspend fun current(priority: Priority): Location {
        requirePermission()

        return suspendCoroutine { continuation ->
            locationDelegate.requestLocation { error, location ->
                if (location != null) {
                    continuation.resume(location.toModel())
                } else {
                    val cause = error?.localizedDescription ?: "Unknown error"
                    continuation.resumeWithException(GeolocationException(cause))
                }
            }
        }
    }

    override suspend fun track(request: LocationRequest): Flow<Location> {
        if (locationDelegate.isTracking) return locationUpdates

        suspendCoroutine { continuation ->
            locationDelegate.trackLocation(request.priority.toIosPriority) { error ->
                if (error == null) continuation.resume(Unit)
                else {
                    val cause = error.localizedDescription
                    continuation.resumeWithException(GeolocationException(cause))
                }
            }
        }

        return locationUpdates
    }

    override fun stopTracking() {
        locationDelegate.stopTracking()
    }

    private suspend fun requirePermission() {
        val state = permissionController.requirePermissionFor(Priority.Balanced)
        if (state != PermissionState.Granted) {
            state.throwOnError()
        }
    }
}