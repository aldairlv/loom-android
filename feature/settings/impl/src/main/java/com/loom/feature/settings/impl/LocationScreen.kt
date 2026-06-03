package com.loom.feature.settings.impl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.loom.core.model.data.UserAccountProfile

@Composable
fun LocationScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocationScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onLocationUpdate = viewModel::updateLocation,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LocationScreenContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onLocationUpdate: (latitude: Double, longitude: Double, city: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var useGps by remember { mutableStateOf(uiState.profile?.locationCoords != null) }
    var searchQuery by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    val placesClient = remember(context) {
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.PLACES_API_KEY)
        }
        Places.createClient(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            getCurrentLocation(context) { lat, lon ->
                onLocationUpdate(lat, lon, null)
            }
        } else {
            useGps = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Location") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Ubicación automática
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ubicación automática",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (useGps) "Usando el GPS del dispositivo" else "Introducir dirección a mano",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Switch(
                    checked = useGps,
                    onCheckedChange = { checked ->
                        useGps = checked
                        if (checked) {
                            val permissions = arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                            if (permissions.all {
                                    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                                }) {
                                getCurrentLocation(context) { lat, lon ->
                                    onLocationUpdate(lat, lon, null)
                                }
                            } else {
                                permissionLauncher.launch(permissions)
                            }
                        }
                    }
                )
            }

            HorizontalDivider()

            // Introducción manual
            if (!useGps) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            if (query.isNotEmpty()) {
                                val request = FindAutocompletePredictionsRequest.builder()
                                    .setQuery(query)
                                    .build()
                                placesClient.findAutocompletePredictions(request)
                                    .addOnSuccessListener { response ->
                                        predictions = response.autocompletePredictions
                                    }
                            } else {
                                predictions = emptyList()
                            }
                        },
                        label = { Text("Buscar dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = ""; predictions = emptyList() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar")
                                }
                            }
                        }
                    )

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(predictions) { prediction ->
                            ListItem(
                                headlineContent = { Text(prediction.getPrimaryText(null).toString()) },
                                supportingContent = { Text(prediction.getSecondaryText(null).toString()) },
                                leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                                modifier = Modifier.clickable {
                                    val placeId = prediction.placeId
                                    val placeFields = listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.NAME)
                                    val request = FetchPlaceRequest.newInstance(placeId, placeFields)
                                    placesClient.fetchPlace(request).addOnSuccessListener { response ->
                                        val latLng = response.place.latLng
                                        if (latLng != null) {
                                            onLocationUpdate(latLng.latitude, latLng.longitude, response.place.name)
                                            searchQuery = response.place.name ?: ""
                                            predictions = emptyList()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Info si GPS está activo
                uiState.profile?.let { profile ->
                    profile.locationCoords?.let { _ ->
                        ListItem(
                            headlineContent = { Text("Ubicación actual detectada") },
                            supportingContent = { Text(profile.city ?: "Coordenadas: ${profile.locationCoords}") },
                            leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context, onLocationResult: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocationResult(location.latitude, location.longitude)
        }
    }
}
