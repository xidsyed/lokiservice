package org.example.project.loki.geolocation

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.example.project.loki.core.InternalCompassApi
import org.example.project.loki.core.Location
import org.example.project.loki.core.Priority
import org.example.project.loki.geolocation.internal.LocationManager
import org.example.project.loki.geolocation.internal.toAndroidLocationRequest
import org.example.project.loki.geolocation.internal.toAndroidPriority
import org.example.project.loki.geolocation.internal.toModel
import org.example.project.loki.permission.LocationPermissionController
import org.example.project.loki.permission.PermissionState
import org.example.project.loki.permission.internal.context.ContextProvider
import org.example.project.loki.permission.throwOnError

/**
 * Creates a new [MobileLocator] instance for Android.
 */
@OptIn(InternalCompassApi::class)
internal actual fun createLocator(
    permissionController: LocationPermissionController,
): MobileLocator {
    return AndroidLocator(ContextProvider.getInstance().context, permissionController)
}

internal class AndroidLocator(
    private val context: Context,
    private val permissionController: LocationPermissionController,
    private val locationManager: LocationManager = LocationManager(context),
) : MobileLocator {

    override val locationUpdates: Flow<Location> = locationManager.locationUpdates
        .mapNotNull { result -> result.lastLocation?.toModel() }

    override suspend fun isAvailable(): Boolean {
        return locationManager.locationEnabled()
    }

    override fun hasPermission(): Boolean {
        return permissionController.hasPermission()
    }

    override suspend fun current(priority: Priority): Location {
        requirePermission(priority)
        return locationManager.currentLocation(priority.toAndroidPriority).toModel()
    }

    override suspend fun track(request: LocationRequest): Flow<Location> {
        requirePermission(request.priority)
        locationManager.startTracking(request.toAndroidLocationRequest())

        return locationUpdates
    }

    override fun stopTracking() {
        locationManager.stopTracking()
    }

    private suspend fun requirePermission(priority: Priority) {
        val state = permissionController.requirePermissionFor(priority)
        if (state != PermissionState.Granted) {
            state.throwOnError()
        }
    }
}