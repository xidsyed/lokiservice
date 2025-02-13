package org.example.project.loki.permission.internal

import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

internal class LocationPermissionManagerDelegate : NSObject(), CLLocationManagerDelegateProtocol {

    internal val manager = CLLocationManager()

    private var permissionCallback: ((CLAuthorizationStatus) -> Unit)? = null

    init {
        manager.delegate = this
    }

    fun currentPermissionStatus(): CLAuthorizationStatus {
        return manager.authorizationStatus
    }

    fun monitorPermission(callback: (CLAuthorizationStatus) -> Unit) {
        permissionCallback = callback
        callback(manager.authorizationStatus)
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        permissionCallback?.invoke(manager.authorizationStatus)
    }

    fun requestPermission(callback: (CLAuthorizationStatus) -> Unit) {
        permissionCallback = callback
        manager.requestAlwaysAuthorization()
    }
}