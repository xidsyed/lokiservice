package org.example.project.loki.geolocation

import org.example.project.loki.permission.LocationPermissionController
import org.example.project.loki.permission.mobile


/**
 * A locator that provides geolocation services on Android and iOS.
 */
public interface MobileLocator : PermissionLocator

/**
 * Create an Android/iOS [MobileLocator] instance for geolocation operations.
 *
 * Make sure you read the [Android documentation](https://developer.android.com/develop/sensors-and-location/location)
 * as well as the [iOS documentation](https://developer.apple.com/documentation/corelocation)
 * to understand the permissions and accuracy.
 *
 * @param permissionController The permission controller to use for handling location permissions.
 * @return A new Android/iOS [MobileLocator] instance.
 */
public fun MobileLocator(
    permissionController: LocationPermissionController = LocationPermissionController.mobile(),
): MobileLocator = createLocator(permissionController)

/**
 * Create an Android/iOS [MobileLocator] instance for geolocation operations.
 *
 * Make sure you read the [Android documentation](https://developer.android.com/develop/sensors-and-location/location)
 * as well as the [iOS documentation](https://developer.apple.com/documentation/corelocation)
 * to understand the permissions and accuracy.
 *
 * @param permissionController The permission controller to use for handling location permissions.
 * @return A new Android/iOS [MobileLocator] instance.
 */
public fun Locator.Companion.mobile(
    permissionController: LocationPermissionController = LocationPermissionController.mobile(),
): MobileLocator = MobileLocator(permissionController)

internal expect fun createLocator(permissionController: LocationPermissionController): MobileLocator