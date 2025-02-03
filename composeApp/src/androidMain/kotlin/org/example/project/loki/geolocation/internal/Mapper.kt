package org.example.project.loki.geolocation.internal

import android.location.Location
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.google.android.gms.location.LocationRequest
import org.example.project.loki.core.Altitude
import org.example.project.loki.core.Azimuth
import org.example.project.loki.core.Coordinates
import org.example.project.loki.core.Priority
import org.example.project.loki.core.Speed
import org.example.project.loki.core.Location as LokiLocation
import org.example.project.loki.geolocation.LocationRequest as LokiLocationRequest


/**
 * Converts a [Location] to a [dev.jordond.compass.Location].
 */
internal fun Location.toModel(): LokiLocation =
    org.example.project.loki.core.Location(
        coordinates = Coordinates(latitude = latitude, longitude = longitude),
        accuracy = accuracy.toDouble(),
        azimuth = Azimuth(
            degrees = bearing,
            accuracy = if (VERSION.SDK_INT < VERSION_CODES.O) null else bearingAccuracyDegrees,
        ),
        speed = Speed(
            mps = speed,
            accuracy = if (VERSION.SDK_INT < VERSION_CODES.O) null else speedAccuracyMetersPerSecond,
        ),
        altitude = Altitude(
            meters = altitude,
            accuracy = if (VERSION.SDK_INT < VERSION_CODES.O) null else verticalAccuracyMeters,
        ),
        timestampMillis = time,
    )

internal val Priority.toAndroidPriority: Int
    get() = when (this) {
        Priority.Balanced -> com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
        Priority.HighAccuracy -> com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
        Priority.LowPower -> com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
        Priority.Passive -> com.google.android.gms.location.Priority.PRIORITY_PASSIVE
    }

internal fun LokiLocationRequest.toAndroidLocationRequest(): LocationRequest {
    return LocationRequest
        .Builder(priority.toAndroidPriority, interval)
        .setGranularity(com.google.android.gms.location.Granularity.GRANULARITY_PERMISSION_LEVEL)
        .build()
}
