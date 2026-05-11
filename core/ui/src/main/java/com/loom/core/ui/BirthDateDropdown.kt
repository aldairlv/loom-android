package com.loom.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateDropdown(
    modifier: Modifier = Modifier,
    placeholder: String,
    selectedValue: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    expanded = true
                }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Text(
                text = if (selectedValue.isBlank()) {
                    placeholder
                } else {
                    selectedValue
                },
                color = if (selectedValue.isBlank())
                    Color.Gray
                else
                    Color.White
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            containerColor = Color(0xFF1E293B)
        ) {

            items.forEach { item ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            color = Color.White
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}