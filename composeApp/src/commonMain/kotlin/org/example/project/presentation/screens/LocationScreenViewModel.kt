package org.example.project.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.app.TestApiKeyConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.Logger
import org.example.project.loki.autocomplete.Autocomplete
import org.example.project.loki.autocomplete.AutocompleteOptions
import org.example.project.loki.autocomplete.AutocompletePlace
import org.example.project.loki.autocomplete.AutocompleteResult
import org.example.project.loki.autocomplete.googlemaps.googleMaps
import org.example.project.loki.core.Location
import org.example.project.loki.core.Place
import org.example.project.loki.core.Priority
import org.example.project.loki.geocoder.Geocoder
import org.example.project.loki.geocoder.GeocoderResult
import org.example.project.loki.geocoder.googlemaps.googleMaps
import org.example.project.loki.geolocation.Geolocator
import org.example.project.loki.geolocation.GeolocatorResult
import org.example.project.loki.geolocation.LocationRequest
import org.example.project.loki.geolocation.TrackingStatus

class LocationScreenViewModel(
    private val geolocator: Geolocator,
    private val geocoder: Geocoder,
    private val autocomplete: Autocomplete<AutocompletePlace>
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init {
        geolocator.trackingStatus
            .onEach { status ->
                Logger.d("TAG", "GeolocationModel: tracking status -> $status")
                updateState { copy(trackingLocation = status) }
            }
            .launchIn(viewModelScope)
    }

    fun currentLocation() {
        viewModelScope.launch {
            updateState { copy(loading = true) }
            val result = geolocator.current(Priority.HighAccuracy)
            updateState { copy(lastResult = result, loading = false) }
        }
    }

    suspend fun isLocationEnabled(): Boolean {
        return geolocator.isAvailable()
    }

    fun clearAutoComplete() {
        updateState { copy(autoCompleteResult = null) }
    }

    fun updateAutoComplete(query: String) {
        viewModelScope.launch {
            val result = autocomplete.search(query)
            updateState { copy(autoCompleteResult = result) }
        }
    }

    fun startTracking() {
        if (state.value.busy) return
        geolocator.track(LocationRequest(priority = Priority.HighAccuracy))
    }

    fun stopTracking() {
        geolocator.stopTracking()
    }

    fun clear() {
        updateState { AppState() }
    }

    private fun updateState(update: AppState.() -> AppState) {
        _state.update(function = update)
    }

    data class AppState(
        val loading: Boolean = false,
        val lastResult: GeolocatorResult? = null,
        val trackingLocation: TrackingStatus = TrackingStatus.Idle,
        val autoCompleteResult: AutocompleteResult<AutocompletePlace>? = null
    ) {
        val tracking = trackingLocation.isActive
        val trackingError = (trackingLocation as? TrackingStatus.Error)?.cause
        val busy: Boolean = loading || tracking
        val permissionsDeniedForever: Boolean =
            lastResult is GeolocatorResult.PermissionDenied && lastResult.forever
    }

    suspend fun getPlace(location: Location): GeocoderResult<Place> {
        updateState { copy(loading = true) }
        val result = geocoder.reverse(location.coordinates)
        updateState { copy(loading = false) }
        return result
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LocationScreenViewModel(
                    geolocator = Geolocator(),
                    geocoder = Geocoder.googleMaps(
                        apiKey = TestApiKeyConfig.googleApiKey,
                        enableLogging = true
                    ),
                    autocomplete = Autocomplete.googleMaps(
                        apiKey = TestApiKeyConfig.googleApiKey,
                        options = AutocompleteOptions(minimumQuery = 4),
                        enableLogging = true,
                    )
                )
            }
        }
    }

}