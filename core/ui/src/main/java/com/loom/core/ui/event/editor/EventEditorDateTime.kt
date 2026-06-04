package com.loom.core.ui.event.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loom.core.ui.LoomWheelPicker
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

@Composable
fun DateInfoSection(
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
fun DateEditorSection(
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
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
fun DateTimeEditSection(
    title: String,
    dateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit
) {
    val days = remember { (1..31).map { it.toString() } }
    val months = remember { Month.entries.map { it.name.take(3) } }
    val years = remember { (2024..2035).map { it.toString() } }
    val hours = remember { (0..23).map { it.toString().padStart(2, '0') } }
    val minutes = remember { (0..59).map { it.toString().padStart(2, '0') } }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            horizontalArrangement = Arrangement.spacedBy(6.dp)
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
            horizontalArrangement = Arrangement.spacedBy(6.dp)
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
