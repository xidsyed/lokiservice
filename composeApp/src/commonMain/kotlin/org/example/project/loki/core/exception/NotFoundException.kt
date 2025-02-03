package org.example.project.loki.core.exception

/**
 * Thrown when a geocoder or geolocation operation returns null.
 */
class NotFoundException : Throwable("Unable to find requested result.")
