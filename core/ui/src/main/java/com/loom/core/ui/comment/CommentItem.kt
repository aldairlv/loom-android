package com.loom.core.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.loom.core.model.data.Comment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

@Composable
fun CommentItem(
    comment: Comment,
    onReplyClick: (Comment) -> Unit,
    modifier: Modifier = Modifier,
    depth: Int = 0
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = (depth * 16).dp)
    ) {
        // Vertical line for Reddit-style trailing (optional, can be added if needed)
        if (depth > 0) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .padding(end = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                // Placeholder for avatar
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Usuario ${comment.profileId.take(5)}", // Simplified for now
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "• 2h", // Simplified
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = if (comment.isDeleted) "[Este comentario ha sido eliminado]" else comment.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (comment.isDeleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row {
                Text(
                    text = "Responder",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onReplyClick(comment) }
                )
            }
        }
    }
}
