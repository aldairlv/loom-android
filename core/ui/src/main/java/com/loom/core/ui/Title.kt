package com.loom.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loom.core.model.data.Post
import com.loom.core.model.data.Title

@Composable
fun TitleHeader(
    title: Title,
    modifier: Modifier = Modifier
) {
    Text(
        text = title.text,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.Black,
        fontSize = 20.sp, // Tamaño más grande
        fontWeight = FontWeight.SemiBold, // Semibold
        style = MaterialTheme.typography.titleLarge
    )
}