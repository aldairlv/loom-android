package com.loom.core.ui.event

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loom.core.model.data.*
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.designsystem.theme.LoomTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun EventFeedCard(
    eventFeed: EventFeedItem,
    onClickLike: () -> Unit,
    onComment: () -> Unit,
    onJoin: () -> Unit = {},
    onBookmark: () -> Unit = {},
    onShare: () -> Unit = {},
    onFollowClick: () -> Unit = {},
    onEventClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val eventDate = remember(eventFeed.eventData.startTime) {
        val dateTime = eventFeed.eventData.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
        val dayOfWeek = when (dateTime.dayOfWeek.name) {
            "MONDAY" -> "Lun"
            "TUESDAY" -> "Mar"
            "WEDNESDAY" -> "Mié"
            "THURSDAY" -> "Jue"
            "FRIDAY" -> "Vie"
            "SATURDAY" -> "Sáb"
            "SUNDAY" -> "Dom"
            else -> ""
        }
        val month = when (dateTime.month.name) {
            "JANUARY" -> "Ene"
            "FEBRUARY" -> "Feb"
            "MARCH" -> "Mar"
            "APRIL" -> "Abr"
            "MAY" -> "May"
            "JUNE" -> "Jun"
            "JULY" -> "Jul"
            "AUGUST" -> "Ago"
            "SEPTEMBER" -> "Sep"
            "OCTOBER" -> "Oct"
            "NOVEMBER" -> "Nov"
            "DECEMBER" -> "Dic"
            else -> ""
        }
        val time = "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
        "$dayOfWeek, $month ${dateTime.dayOfMonth} • $time"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { 
                android.util.Log.d("LOOM_EVENT_DETAIL", "Card: Clicking on event: ${eventFeed.id}")
                onEventClick(eventFeed.id) 
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top
            ) {
                // Square Image with Creator Avatar Overlay
                Box(modifier = Modifier.size(50.dp)) {
                    val eventImageUrl = eventFeed.eventData.assets.firstOrNull()?.url 
                        ?: eventFeed.eventData.thumbnailUrl
                    if (eventImageUrl != null) {
                        AsyncImage(
                            model = eventImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = eventFeed.eventData.category?.take(1)?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Creator Avatar Overlay
                    AsyncImage(
                        model = eventFeed.creator.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 6.dp, y = 6.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = eventFeed.eventData.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { 
                            android.util.Log.d("LOOM_EVENT_DETAIL", "Card: Clicking on event title: ${eventFeed.id}")
                            onEventClick(eventFeed.id) 
                        }
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Por ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                                append(eventFeed.creator.displayName)
                            }
                            val distanceText = eventFeed.distance?.let { " · ${"%.1f".format(it)} km" } ?: ""
                            append(distanceText)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.offset(y = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Carousel
            if (eventFeed.eventData.assets.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = { eventFeed.eventData.assets.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) { page ->
                    AsyncImage(
                        model = eventFeed.eventData.assets[page].url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date and Location Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = eventDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                eventFeed.eventData.location?.let { location ->
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        onClick = { /* TODO */ },
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = location.address ?: location.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Friends/Attendees
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (eventFeed.friendsAttending.isNotEmpty()) {
                    Box(modifier = Modifier.width((eventFeed.friendsAttending.take(3).size * 16 + 8).dp)) {
                        eventFeed.friendsAttending.take(3).forEachIndexed { index, friend ->
                            AsyncImage(
                                model = friend.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = (index * 16).dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    val friendsCount = eventFeed.friendsAttending.size
                    val othersCount = (eventFeed.eventData.rsvpCount - friendsCount).coerceAtLeast(0)
                    Text(
                        text = if (othersCount > 0) {
                            "$friendsCount amigos y $othersCount más"
                        } else {
                            "$friendsCount amigos"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                } else {
                    Text(
                        text = "${eventFeed.eventData.rsvpCount} asistentes",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tags
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                eventFeed.tags.forEach { tag ->
                    Text(
                        text = "#$tag",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions: comentario, unirse, bookmark y like
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onComment, modifier = Modifier.weight(1f)) {
                    Icon(LoomIcons.CommentBorder, contentDescription = "Comment")
                }
                IconButton(onClick = onJoin, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Join")
                }
                IconButton(onClick = onBookmark, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark")
                }
                IconButton(onClick = onClickLike, modifier = Modifier.weight(1f)) {
                    Icon(LoomIcons.LikeBorder, contentDescription = "Like")
                }
            }
        }
    }
}


@Preview
@Composable
fun EventFeedCardPreview() {
    val sampleEvent = EventFeedItem(
        id = "1",
        timestamp = Clock.System.now().toEpochMilliseconds(),
        tags = listOf("Concierto", "Música"),
        creator = EventCreator(
            displayName = "Aldair",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80"
        ),
        eventData = EventData(
            title = "Gran Concierto de Verano con muchos artistas invitados y sorpresas",
            description = "Ven a disfrutar de la mejor música en vivo con artistas locales e internacionales. Una noche inolvidable te espera en el corazón de la ciudad.",
            assets = listOf(
                PostMedia(
                    id = "a1",
                    url = "https://images.unsplash.com/photo-1501281668745-f7f57925c3b4?ixlib=rb-4.0.3&auto=format&fit=crop&w=1000&q=80",
                    type = "IMAGE",
                    width = 1080,
                    height = 720
                )
            ),
            startTime = Clock.System.now(),
            endTime = Clock.System.now().plus(4.hours),
            location = EventLocation(
                name = "Estadio Nacional",
                address = "Calle Principal 123",
                coordinates = EventCoordinates(-12.067, -77.033)
            ),
            rsvpCount = 150,
            maxAttendees = 500,
            isOnline = false,
            isPublic = true,
            isCancelled = false,
            status = "PUBLISHED",
            category = "Música"
        ),
        friendsAttending = listOf(
            FriendAttending("f1", "Juan", "https://images.unsplash.com/photo-1599566150163-29194dcaad36?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80"),
            FriendAttending("f2", "Maria", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80")
        ),
        distance = 1.5
    )

    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EventFeedCard(
                eventFeed = sampleEvent,
                onClickLike = {},
                onComment = {},
                onJoin = {},
                onBookmark = {},
                onShare = {},
                onFollowClick = {},
                onEventClick = {}
            )
        }
    }
}
