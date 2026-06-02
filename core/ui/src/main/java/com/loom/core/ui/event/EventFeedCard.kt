package com.loom.core.ui.event

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loom.core.model.data.EventFeedItem
import com.loom.core.designsystem.icon.LoomIcons
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun EventFeedCard(
    eventFeed: EventFeedItem,
    onClickLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val eventDate = remember(eventFeed.eventData.startTime) {
        val dateTime = eventFeed.eventData.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
        val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        "${dateTime.dayOfMonth} $monthName"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = eventFeed.creator.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = eventFeed.creator.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Organizador",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                TextButton(onClick = onFollowClick) {
                    Text("Seguir")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Event Image & Date Overlay
            Box {
                eventFeed.eventData.assets.firstOrNull()?.let { asset ->
                    AsyncImage(
                        model = asset.url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = eventDate,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title and Description
            Text(
                text = eventFeed.eventData.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = eventFeed.eventData.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Location, Distance and Category
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${eventFeed.eventData.location.name}${eventFeed.distance?.let { " • $it" } ?: ""}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                SuggestionChip(
                    onClick = { },
                    label = { Text(eventFeed.eventData.category) }
                )
            }

            // Friends Attending
            if (eventFeed.friendsAttending.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width((eventFeed.friendsAttending.take(3).size * 16 + 8).dp)) {
                        eventFeed.friendsAttending.take(3).forEachIndexed { index, friend ->
                            AsyncImage(
                                model = friend.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = (index * 16).dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    val othersCount = eventFeed.eventData.rsvpCount - eventFeed.friendsAttending.size
                    if (othersCount > 0) {
                        Text(
                            text = "y $othersCount usuarios más",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    } else if (eventFeed.friendsAttending.size > 0) {
                        Text(
                            text = if (eventFeed.friendsAttending.size == 1) "asiste" else "asisten",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(onClick = onComment) {
                        Icon(LoomIcons.CommentBorder, contentDescription = "Comment")
                    }
                    IconButton(onClick = onClickLike) {
                        Icon(LoomIcons.LikeBorder, contentDescription = "Like")
                    }
                }
                IconButton(onClick = onShare) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    }
}
