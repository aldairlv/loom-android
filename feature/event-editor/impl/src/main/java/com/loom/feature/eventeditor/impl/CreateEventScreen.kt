package com.loom.feature.eventeditor.impl

import android.location.Geocoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.LoomWheelPicker
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import java.util.Locale

@Composable
fun EventEditorScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventEditorScreen(
        modifier = modifier,
        uiState = uiState,
        onClose = onClose,
        onImagesSelected = viewModel::onImagesSelected,
        onThumbnailSelected = viewModel::onThumbnailSelected,
        onTitleChange = viewModel::onTitleChange,
        onToggleDatePicker = viewModel::toggleDatePicker,
        onToggleEndEnabled = viewModel::onToggleEndEnabled,
        onUpdateStartTime = viewModel::updateStartTime,
        onUpdateEndTime = viewModel::updateEndTime,
        onCancelDate = viewModel::cancelDateSelection,
        onSaveDate = viewModel::saveDateSelection,
        // Location callbacks
        onToggleLocationPicker = viewModel::toggleLocationPicker,
        onLocationNameChange = viewModel::onLocationNameChange,
        onLocationSelected = viewModel::onLocationSelected,
        onCancelLocation = viewModel::cancelLocationSelection,
        onSaveLocation = viewModel::saveLocationSelection
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventEditorScreen(
    modifier: Modifier = Modifier,
    uiState: CreateEventUiState,
    onClose: () -> Unit,
    onImagesSelected: (List<Uri>) -> Unit,
    onThumbnailSelected: (Uri) -> Unit,
    onTitleChange: (String) -> Unit,
    onToggleDatePicker: () -> Unit,
    onToggleEndEnabled: (Boolean) -> Unit,
    onUpdateStartTime: (LocalDateTime) -> Unit,
    onUpdateEndTime: (LocalDateTime) -> Unit,
    onCancelDate: () -> Unit,
    onSaveDate: () -> Unit,
    onToggleLocationPicker: () -> Unit,
    onLocationNameChange: (String) -> Unit,
    onLocationSelected: (Double, Double, String) -> Unit,
    onCancelLocation: () -> Unit,
    onSaveLocation: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> if (uris.isNotEmpty()) onImagesSelected(uris) }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            CreateEventTopBar(
                onClose = onClose,
                onCreateClick = { /* Create event logic */ }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                PhotoArea(
                    uris = uiState.allUris,
                    onAddPhotosClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            item {
                TitleInput(
                    title = uiState.title,
                    onTitleChange = onTitleChange
                )
            }

            item {
                DateInfoSection(
                    formattedDateRange = uiState.formattedDateRange,
                    formattedTimeRange = uiState.formattedTimeRange,
                    onEditClick = onToggleDatePicker
                )
            }

            item {
                AnimatedVisibility(visible = uiState.isDatePickerVisible) {
                    DateEditorSection(
                        startTime = uiState.startTime,
                        endTime = uiState.endTime,
                        isEndEnabled = uiState.isEndEnabled,
                        onUpdateStartTime = onUpdateStartTime,
                        onUpdateEndTime = onUpdateEndTime,
                        onToggleEndEnabled = onToggleEndEnabled,
                        onCancel = onCancelDate,
                        onSave = onSaveDate
                    )
                }
            }

            item {
                LocationInfoSection(
                    locationName = uiState.locationName.ifBlank { "Location Name" },
                    locationAddress = uiState.locationAddress.ifBlank { "Location Address" },
                    onEditClick = onToggleLocationPicker
                )
            }

            item {
                AnimatedVisibility(visible = uiState.isLocationPickerVisible) {
                    LocationEditorSection(
                        locationName = uiState.locationName,
                        locationAddress = uiState.locationAddress,
                        latitude = uiState.latitude,
                        longitude = uiState.longitude,
                        onLocationNameChange = onLocationNameChange,
                        onLocationSelected = onLocationSelected,
                        onCancel = onCancelLocation,
                        onSave = onSaveLocation
                    )
                }
            }

            if (uiState.allUris.isNotEmpty()) {
                item {
                    ThumbnailSelectorSection(
                        allUris = uiState.allUris,
                        thumbnailUri = uiState.thumbnailUri,
                        onThumbnailSelected = onThumbnailSelected
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEventTopBar(
    onClose: () -> Unit,
    onCreateClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        actions = {
            TextButton(onClick = onCreateClick) {
                Text("Create", style = MaterialTheme.typography.labelLarge)
            }
            IconButton(onClick = { /* More actions */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    )
}

@Composable
private fun PhotoArea(
    uris: List<Uri>,
    onAddPhotosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onAddPhotosClick() },
        contentAlignment = Alignment.Center
    ) {
        if (uris.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Photos",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Text(
                    "Add Photos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        } else {
            val pagerState = rememberPagerState(pageCount = { uris.size })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = uris[page],
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add more",
                    modifier = Modifier.size(56.dp),
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun TitleInput(
    title: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        BasicTextField(
            value = title,
            onValueChange = { newTitle ->
                if (!newTitle.contains("\n")) {
                    onTitleChange(newTitle)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = "Event Title...",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun DateInfoSection(
    formattedDateRange: String,
    formattedTimeRange: String,
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
            text = "📅",
            fontSize = 32.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formattedDateRange,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formattedTimeRange,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Edit Date",
                tint = Color(0xFFE91E63)
            )
        }
    }
}

@Composable
private fun LocationInfoSection(
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
private fun DateEditorSection(
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    isEndEnabled: Boolean,
    onUpdateStartTime: (LocalDateTime) -> Unit,
    onUpdateEndTime: (LocalDateTime) -> Unit,
    onToggleEndEnabled: (Boolean) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "When?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        DateTimeEditSection(
            title = "Inicio",
            dateTime = startTime,
            onDateTimeChange = onUpdateStartTime
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "End",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = isEndEnabled,
                onCheckedChange = onToggleEndEnabled
            )
        }

        if (isEndEnabled) {
            DateTimeEditSection(
                title = "Fin",
                dateTime = endTime,
                onDateTimeChange = onUpdateEndTime
            )
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

@Composable
private fun LocationEditorSection(
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Where?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Lugar (Location Name)
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

        // Dirección y Mapa
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
                        // Get address from latLng
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

@Composable
private fun ThumbnailSelectorSection(
    allUris: List<Uri>,
    thumbnailUri: Uri?,
    onThumbnailSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Select thumbnail:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(allUris) { uri ->
                ThumbnailItem(
                    uri = uri,
                    isSelected = uri == thumbnailUri,
                    onClick = { onThumbnailSelected(uri) }
                )
            }
        }
    }
}

@Composable
private fun DateTimeEditSection(
    title: String,
    dateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit
) {
    val days = remember { (1..31).map { it.toString() } }
    val months = remember { Month.entries.map { it.name.take(3) } }
    val years = remember { (2024..2035).map { it.toString() } }
    val hours = remember { (0..23).map { it.toString().padStart(2, '0') } }
    val minutes = remember { (0..59).map { it.toString().padStart(2, '0') } }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 2.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                LoomWheelPicker(
                    items = days,
                    initialIndex = dateTime.dayOfMonth - 1,
                    onItemSelected = { index ->
                        val newDay = index + 1
                        if (newDay != dateTime.dayOfMonth) {
                            onDateTimeChange(LocalDateTime(dateTime.year, dateTime.month, newDay, dateTime.hour, dateTime.minute))
                        }
                    }
                )
                Text("Day", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(modifier = Modifier.weight(1.2f), horizontalAlignment = Alignment.CenterHorizontally) {
                LoomWheelPicker(
                    items = months,
                    initialIndex = dateTime.monthNumber - 1,
                    onItemSelected = { index ->
                        val newMonth = Month.entries[index]
                        if (newMonth != dateTime.month) {
                            onDateTimeChange(LocalDateTime(dateTime.year, newMonth, dateTime.dayOfMonth, dateTime.hour, dateTime.minute))
                        }
                    }
                )
                Text("Month", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(modifier = Modifier.weight(1.2f), horizontalAlignment = Alignment.CenterHorizontally) {
                LoomWheelPicker(
                    items = years,
                    initialIndex = years.indexOf(dateTime.year.toString()).coerceAtLeast(0),
                    onItemSelected = { index ->
                        val newYear = years[index].toInt()
                        if (newYear != dateTime.year) {
                            onDateTimeChange(LocalDateTime(newYear, dateTime.month, dateTime.dayOfMonth, dateTime.hour, dateTime.minute))
                        }
                    }
                )
                Text("Year", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                LoomWheelPicker(
                    items = hours,
                    initialIndex = dateTime.hour,
                    onItemSelected = { index ->
                        if (index != dateTime.hour) {
                            onDateTimeChange(LocalDateTime(dateTime.year, dateTime.month, dateTime.dayOfMonth, index, dateTime.minute))
                        }
                    }
                )
                Text("Hour", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                LoomWheelPicker(
                    items = minutes,
                    initialIndex = dateTime.minute,
                    onItemSelected = { index ->
                        if (index != dateTime.minute) {
                            onDateTimeChange(LocalDateTime(dateTime.year, dateTime.month, dateTime.dayOfMonth, dateTime.hour, index))
                        }
                    }
                )
                Text("Min", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ThumbnailItem(
    uri: Uri,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Transparent, CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventEditorScreenPreview() {
    LoomTheme {
        EventEditorScreen(
            uiState = CreateEventUiState(
                title = "Aniversario de Loom",
                isDatePickerVisible = false,
                isLocationPickerVisible = true,
                locationName = "Sede Central",
                locationAddress = "Av. Siempre Viva 742"
            ),
            onClose = {},
            onImagesSelected = {},
            onThumbnailSelected = {},
            onTitleChange = {},
            onToggleDatePicker = {},
            onToggleEndEnabled = {},
            onUpdateStartTime = {},
            onUpdateEndTime = {},
            onCancelDate = {},
            onSaveDate = {},
            onToggleLocationPicker = {},
            onLocationNameChange = {},
            onLocationSelected = { _, _, _ -> },
            onCancelLocation = {},
            onSaveLocation = {}
        )
    }
}
