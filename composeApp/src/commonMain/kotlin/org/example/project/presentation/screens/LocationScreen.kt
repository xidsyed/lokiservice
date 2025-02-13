package org.example.project.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.loki.autocomplete.AutocompletePlace
import org.example.project.loki.autocomplete.AutocompleteResult
import org.example.project.loki.core.Place
import org.example.project.loki.geolocation.GeolocatorResult
import org.example.project.loki.permission.LocationPermissionController
import org.example.project.loki.permission.openSettings
import org.example.project.presentation.SnackbarHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(snackbarHandler: SnackbarHandler , viewModel: LocationScreenViewModel) {

    var text by remember { mutableStateOf("") }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.lastResult?.isError == true && state.busy) {
        if (state.permissionsDeniedForever) {
            snackbarHandler(
                message = "Location permissions denied forever",
                actionLabel = "Settings",
                action = LocationPermissionController::openSettings,
                duration = SnackbarDuration.Short
            )
        } else if (!viewModel.isLocationEnabled()) {
            snackbarHandler(
                message = "Turn on Location",
                actionLabel = null,
                duration = SnackbarDuration.Short,
                action = null
            )
        } else if (state.lastResult is GeolocatorResult.GeolocationFailed) {
            snackbarHandler(
                message = (state.lastResult as GeolocatorResult.GeolocationFailed).message,
                actionLabel = null,
                duration = SnackbarDuration.Short,
                action = null
            )
        }
    }

    var locationCleared by remember { mutableStateOf(true) }

    LaunchedEffect(state.lastResult?.getOrNull()) {
        state.lastResult?.getOrNull()?.let { location ->
            val place = viewModel.getPlace(location)
            place.getFirstOrNull()?.let { p ->
                text = p.toAddress()
                locationCleared = false
            }

        }
    }

    Column(
        Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.busy) {
            CircularProgressIndicator()
        } else {

            var expanded =
                remember { derivedStateOf { state.autoCompleteResult != null } }.value

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                LocationSearchBar(
                    modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                    onLocationClick = viewModel::currentLocation,
                    locationCleared = locationCleared,
                    text = text,
                    onClear = { text = ""; locationCleared = true; viewModel.clear() },
                    onValueChange = { text = it; viewModel.updateAutoComplete(it) }
                )

                ExposedDropdownMenu(
                    modifier = Modifier
                        .animateContentSize(),
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                    matchTextFieldWidth = true,
                    expanded = expanded,
                    onDismissRequest = viewModel::clearAutoComplete,
                    scrollState = rememberScrollState(),
                ) {
                    if (state.autoCompleteResult is AutocompleteResult.Success) {
                        (state.autoCompleteResult as AutocompleteResult.Success<AutocompletePlace>).data.forEach { p ->
                            DropdownMenuItem(
                                onClick = {
                                    text = p.address
                                    viewModel.clearAutoComplete()
                                },
                                text = {
                                    Text(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        text = p.address,
                                        maxLines = 3,
                                        minLines = 2,
                                    )
                                })
                        }
                    }
                }
            }

        }

    }
}


@Composable
fun LocationSearchBar(
    modifier: Modifier,
    onLocationClick: () -> Unit,
    locationCleared: Boolean,
    text: String,
    onClear: () -> Unit,
    onValueChange: (String) -> Unit,

    ) {
    val shape = RoundedCornerShape(12.dp)
    _root_ide_package_.androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = shape)
            .background(color = _root_ide_package_.androidx.compose.ui.graphics.Color.White)
            .border(
                border = _root_ide_package_.androidx.compose.foundation.BorderStroke(
                    0.5.dp,
                    _root_ide_package_.androidx.compose.ui.graphics.Color.Gray
                ),
                shape = shape
            )
            .padding(
                start = 16.dp, end = 12.dp, top = 18.dp, bottom = 18.dp
            )
    ) {


        var hasFocus by remember { mutableStateOf(false) }
        _root_ide_package_.androidx.compose.foundation.text.BasicTextField(
            value = text.ifBlank {
                if (hasFocus) "" else "Enter a location"
            },
            onValueChange = onValueChange,
            modifier = Modifier
                .onFocusChanged { hasFocus = it.hasFocus }
                .fillMaxWidth()
                .weight(1f),
            maxLines = 2, // Allow overflow content
            minLines = 2,
            textStyle = _root_ide_package_.androidx.compose.material3.LocalTextStyle.current.copy(
                color = if (text.isBlank()) _root_ide_package_.androidx.compose.ui.graphics.Color.Gray else _root_ide_package_.androidx.compose.ui.graphics.Color.Unspecified
            ),
            singleLine = false
        )
        _root_ide_package_.androidx.compose.animation.AnimatedContent(
            locationCleared,
            label = "load image"
        ) {
            when (it) {
                true -> {
                    _root_ide_package_.androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .clip(shape)
                            .clickable(onClick = onLocationClick)
                            .height(48.dp)
                            .width(48.dp)
                    ) {

                        _root_ide_package_.androidx.compose.material3.Icon(
                            imageVector = _root_ide_package_.androidx.compose.material.icons.Icons.Rounded.LocationOn,
                            contentDescription = "Location Icon",
                            tint = _root_ide_package_.androidx.compose.ui.graphics.Color.Gray,
                            modifier = Modifier
                                .size(size = 32.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                false -> {
                    _root_ide_package_.androidx.compose.material3.Icon(
                        modifier = Modifier
                            .clip(shape)
                            .clickable(onClick = onClear)
                            .height(24.dp)
                            .width(24.dp),
                        imageVector = _root_ide_package_.androidx.compose.material.icons.Icons.Rounded.Close,
                        contentDescription = "Clear",
                        tint = _root_ide_package_.androidx.compose.ui.graphics.Color.Gray,
                    )
                }

            }
        }
    }

}


fun Place.toAddress(): String =
    "${name ?: ""} ${street ?: ""} ${locality ?: ""}\n${postalCode ?: ""}"
