package com.loom.core.ui.post

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.LayoutRoot
import com.loom.core.model.data.LayoutRow
import com.loom.core.model.data.PostInteractions
import com.loom.core.model.data.PostStats
import kotlinx.datetime.Clock


@Composable
fun PostFeedCard(
    postFeed: PostFeedItem,
    onClickLike: () -> Unit,
    onComment: () -> Unit,
    onRepost: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding( vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            // Header: Author Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = postFeed.author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = postFeed.author.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    // Smart cast fix with local variable
                    val parent = postFeed.parent
                    if (parent != null) {
                        Text(
                            text = "Reposteado de ${parent.author.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Root author if different from parent
            val root = postFeed.root
            val parent = postFeed.parent
            if (root != null && root.id != parent?.id) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    AsyncImage(
                        model = root.author.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = root.author.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Trail
            if (postFeed.trail.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            MaterialTheme.shapes.small
                        )
                        .padding(8.dp)
                ) {
                    postFeed.trail.forEach { trailItem ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = trailItem.author.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = trailItem.author.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // Simple preview of trail content (first text if exists)
                        trailItem.contents.firstOrNull { it.type == "text" }?.text?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(start = 28.dp, top = 2.dp, bottom = 4.dp)
                            )
                        }
                    }
                }
            }

            // Contents
            val layoutRoot = postFeed.layout.firstOrNull { it.type == "rows" }
            if (layoutRoot != null) {
                layoutRoot.display.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.blocks.forEach { blockIndex ->
                            val content = postFeed.contents.getOrNull(blockIndex)
                            if (content != null) {
                                Box(modifier = Modifier.weight(1f)) {
                                    RenderContent(content)
                                }
                            }
                        }
                    }
                }
            } else {
                // Fallback: simple vertical list
                postFeed.contents.sortedBy { it.order }.forEach { content ->
                    RenderContent(content)
                }
            }

            // Tags
            if (postFeed.tags.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                val tagsText = postFeed.tags.joinToString(" ") { "#$it" }

                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = tagsText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!expanded && postFeed.tags.size > 2) {
                        Text(
                            text = "ver todas",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    }
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onComment) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment")
                    }
                    postFeed.stats?.commentsCount?.let {
                        if (it > 0) Text(text = it.toString(), style = MaterialTheme.typography.labelMedium)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onRepost) {
                        Icon(
                            Icons.Default.Repeat,
                            contentDescription = "Repost",
                            tint = if (postFeed.interactions?.reposted == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    postFeed.stats?.repostsCount?.let {
                        if (it > 0) Text(text = it.toString(), style = MaterialTheme.typography.labelMedium)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = onClickLike) {
                        Icon(
                            imageVector = if (postFeed.interactions?.liked == true) Icons.Default.FavoriteBorder else Icons.Default.FavoriteBorder, // TODO: Use filled icon for liked
                            contentDescription = "Like",
                            tint = if (postFeed.interactions?.liked == true) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    postFeed.stats?.likesCount?.let {
                        if (it > 0) Text(text = it.toString(), style = MaterialTheme.typography.labelMedium)
                    }
                }
                IconButton(onClick = onShare, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    }
}

@Composable
private fun RenderContent(content: PostFeedContent) {
    when (content.type) {
        "text" -> {
            content.text?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        "image" -> {
            content.media?.let { media ->
                val aspectRatio = if (media.height > 0) media.width.toFloat() / media.height.toFloat() else 1f
                AsyncImage(
                    model = media.url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop
                )
            }
        }
        "video" -> {
            content.media?.let { media ->
                val aspectRatio = if (media.height > 0) media.width.toFloat() / media.height.toFloat() else 1f
                val context = LocalContext.current

                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        val mediaItem = MediaItem.fromUri(Uri.parse(media.url))
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = false
                        repeatMode = Player.REPEAT_MODE_ONE
                    }
                }

                var isPlaying by remember { mutableStateOf(false) }

                LaunchedEffect(exoPlayer) {
                    val listener = object : Player.Listener {
                        override fun onIsPlayingChanged(playing: Boolean) {
                            isPlaying = playing
                        }
                    }
                    exoPlayer.addListener(listener)
                }

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = false
                                setBackgroundColor(android.graphics.Color.BLACK)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (!isPlaying) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .clickable {
                                    exoPlayer.playWhenReady = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    exoPlayer.playWhenReady = false
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PostFeedCardPreview() {
    val fakePost = PostFeedItem(
        id = "1",
        author = PostAuthor(
            id = "author1",
            displayName = "Aldair",
            avatarUrl = "https://example.com/avatar.jpg"
        ),
        parent = PostParent(
            id = "parent1",
            author = PostAuthor(
                id = "author2",
                displayName = "Jane Doe",
                avatarUrl = "https://example.com/avatar2.jpg"
            )
        ),
        root = PostParent(
            id = "root1",
            author = PostAuthor(
                id = "author3",
                displayName = "Root Author",
                avatarUrl = "https://example.com/avatar3.jpg"
            )
        ),
        status = "published",
        tags = listOf("android", "compose", "kotlin", "ui", "development"),
        contents = listOf(
            PostFeedContent(
                id = 1,
                type = "text",
                order = 0,
                text = "¡Hola! Este es un post de prueba para el feed de Loom. ¿Qué les parece el diseño?",
                media = null
            ),
            PostFeedContent(
                id = 2,
                type = "image",
                order = 1,
                text = null,
                media = PostMedia(
                    id = "media1",
                    url = "https://http.cat/200",
                    type = "image",
                    width = 1080,
                    height = 720
                )
            )
        ),
        layout = listOf(
            LayoutRoot(
                type = "rows",
                display = listOf(
                    LayoutRow(blocks = listOf(0)),
                    LayoutRow(blocks = listOf(1))
                )
            )
        ),
        interactions = PostInteractions(liked = false, reposted = false, commented = false),
        stats = PostStats(likesCount = 10, repostsCount = 5, commentsCount = 2),
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now(),
        publishedAt = Clock.System.now()
    )

    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PostFeedCard(
                postFeed = fakePost,
                onClickLike = {},
                onComment = {},
                onRepost = {},
                onShare = {}
            )
        }
    }
}
