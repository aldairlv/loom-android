package com.loom.core.ui.event.editor

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.loom.core.ui.BuildConfig
import java.util.Locale

@Composable
fun LocationInfoSection(
    locationName: String,
    locationAddress: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "📍",
            fontSize = 32.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = locationName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = locationAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Edit Location",
                tint = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun LocationEditorSection(
    locationName: String,
    locationAddress: String,
    latitude: Double,
    longitude: Double,
    onLocationNameChange: (String) -> Unit,
    onLocationSelected: (Double, Double, String) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }

    LaunchedEffect(context) {
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.MAPS_API_KEY)
        }
    }
    
    val initialPos = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 15f)
    }
    
    val markerState = rememberMarkerState(position = initialPos)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Where?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Lugar", style = MaterialTheme.typography.labelMedium)
            BasicTextField(
                value = locationName,
                onValueChange = onLocationNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    if (locationName.isEmpty()) {
                        Text("Nombre del lugar...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    }
                    innerTextField()
                }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Address", style = MaterialTheme.typography.labelMedium)
            Text(
                text = locationAddress.ifBlank { "Toca el mapa para marcar la dirección" },
                style = MaterialTheme.typography.bodyMedium,
                color = if (locationAddress.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        markerState.position = latLng
                        try {
                            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                            val addressText = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown address"
                            onLocationSelected(latLng.latitude, latLng.longitude, addressText)
                        } catch (e: Exception) {
                            onLocationSelected(latLng.latitude, latLng.longitude, "Error fetching address")
                        }
                    }
                ) {
                    Marker(state = markerState)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }
        }
    }
}
