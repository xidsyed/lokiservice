package org.example.project.loki.geolocation

import org.example.project.loki.core.Priority

/**
 * Represents a request for location updates.
 *
 * @param priority The priority of the request.
 * @param interval The interval at which to receive location updates, in milliseconds.
 * @param maximumAge The maximum age of a cached location that can be used before a new location is
 * requested, in milliseconds.
 * @param ignoreAvailableCheck Whether to ignore the availability of location services when
 * requesting a location. If `true`, the request will be made regardless of whether location
 * services are available, potentially resulting in an error if they are not.
 */

public class LocationRequest(
    public val priority: Priority = Priority.Balanced,
    public val interval: Long = 5000L,
    public val maximumAge: Long = 0L,
    public val ignoreAvailableCheck: Boolean = false,
)