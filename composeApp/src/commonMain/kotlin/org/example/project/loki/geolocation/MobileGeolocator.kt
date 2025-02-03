package org.example.project.loki.geolocation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.example.project.loki.permission.LocationPermissionController
import org.example.project.loki.permission.mobile

/**
 * Create a new [Geolocator] instance for geolocation operations.
 *
 * Make sure you read the [Android documentation](https://developer.android.com/develop/sensors-and-location/location)
 * as well as the [iOS documentation](https://developer.apple.com/documentation/corelocation)
 * to understand the permissions and accuracy.
 *
 * @param permissionController The [LocationPermissionController] to use for handling permissions.
 * @param dispatcher The [CoroutineDispatcher] to use for geolocation operations.
 * @return A new [Geolocator] instance.
 */
public fun Geolocator(
    permissionController: LocationPermissionController = LocationPermissionController.mobile(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geolocator = MobileGeolocator(permissionController, dispatcher)

/**
 * Create a new [Geolocator] instance for geolocation operations.
 *
 * Make sure you read the [Android documentation](https://developer.android.com/develop/sensors-and-location/location)
 * as well as the [iOS documentation](https://developer.apple.com/documentation/corelocation)
 * to understand the permissions and accuracy.
 *
 * @param permissionController The [LocationPermissionController] to use for handling permissions.
 * @param dispatcher The [CoroutineDispatcher] to use for geolocation operations.
 * @return A new [Geolocator] instance.
 */
@Suppress("FunctionName")
public fun MobileGeolocator(
    permissionController: LocationPermissionController = LocationPermissionController.mobile(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geolocator = Geolocator(MobileLocator(permissionController), dispatcher)

/**
 * Create a new [Geolocator] instance for geolocation operations.
 *
 * Make sure you read the [Android documentation](https://developer.android.com/develop/sensors-and-location/location)
 * as well as the [iOS documentation](https://developer.apple.com/documentation/corelocation)
 * to understand the permissions and accuracy.
 *
 * @param permissionController The [LocationPermissionController] to use for handling permissions.
 * @param dispatcher The [CoroutineDispatcher] to use for geolocation operations.
 * @return A new [Geolocator] instance.
 */
public fun Geolocator.Companion.mobile(
    permissionController: LocationPermissionController = LocationPermissionController.mobile(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Geolocator = MobileGeolocator(permissionController, dispatcher)