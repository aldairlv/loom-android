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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.loom.core.common.util.toRelativeTimeSpan
import com.loom.core.designsystem.icon.LoomIcons
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFeedCard(
    postFeed: PostFeedItem,
    onClickLike: () -> Unit,
    onComment: () -> Unit,
    onQuickRepost: () -> Unit,
    onCommentRepost: (com.loom.core.model.data.PostFeedItem) -> Unit,
    onShare: () -> Unit,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.runtime.LaunchedEffect(postFeed.id) {
        android.util.Log.d("LOOM_DATA_FLOW", "UI PostFeedCard: id=${postFeed.id}, hasRoot=${postFeed.root != null}, rootContentSize=${postFeed.root?.contents?.size ?: 0}")
    }
    var showRepostSheet by remember { mutableStateOf(false) }

    if (showRepostSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRepostSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                ListItem(
                    headlineContent = { Text("Reposteo rápido") },
                    leadingContent = { Icon(Icons.Default.Repeat, contentDescription = null) },
                    modifier = Modifier.clickable {
                        onQuickRepost()
                        showRepostSheet = false
                    }
                )
                ListItem(
                    headlineContent = { Text("Repostear con comentario") },
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                    modifier = Modifier.clickable {
                        onCommentRepost(postFeed)
                        showRepostSheet = false
                    }
                )
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding( vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            // Header: Author Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                AsyncImage(
                    model = postFeed.author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = postFeed.author.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    val timeSpan = postFeed.publishedAt?.toRelativeTimeSpan() ?: postFeed.createdAt.toRelativeTimeSpan()
                    val parent = postFeed.parent
                    val root = postFeed.root

                    if (parent != null && root != null) {
                        Text(
                            text = if (parent.author.id == root.author.id) "Reposteado • $timeSpan" else "Ha reposteado a ${parent.author.displayName} • $timeSpan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    } else {
                        Text(
                            text = timeSpan,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                if (!postFeed.author.isFollowed) {
                    TextButton(onClick = onFollowClick) {
                        Text(
                            text = "Seguir",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            PostFeedContentBody(postFeed = postFeed)



            // Tags
            if (postFeed.tags.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                val tagsText = postFeed.tags.joinToString(" ") { "#$it" }

                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
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
                        Icon(
                            imageVector = if (postFeed.interactions?.commented == true) LoomIcons.Comment else LoomIcons.CommentBorder,
                            contentDescription = "Comment",
                            tint = if (postFeed.interactions?.commented == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    postFeed.stats?.commentsCount?.let {
                        if (it > 0) Text(text = it.toString(), style = MaterialTheme.typography.labelMedium)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(onClick = { showRepostSheet = true }) {
                        Icon(
                            imageVector = if (postFeed.interactions?.reposted == true) LoomIcons.Repost else LoomIcons.RepostBorder,
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
                            imageVector = if (postFeed.interactions?.liked == true) LoomIcons.Like else LoomIcons.LikeBorder,
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
internal fun PostFeedContentBody(
    postFeed: PostFeedItem,
    modifier: Modifier = Modifier
) {
    android.util.Log.d("LOOM_REPOST_DEBUG", "PostFeedContentBody: Rendering. ID=${postFeed.id}, root=${postFeed.root != null}, contentsSize=${postFeed.contents.size}")
    val root = postFeed.root
    val parent = postFeed.parent

    Column(modifier = modifier) {
        // Root content
        if (root != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp)
                ) {
                    AsyncImage(
                        model = root.author.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = root.author.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                PostLayoutContent(
                    contents = root.contents ?: emptyList(),
                    layout = root.layout
                )
            }
        }

        // Trail
        if (postFeed.trail.isNotEmpty()) {
            Column {
                postFeed.trail.forEach { trailItem ->
                    Column(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = trailItem.author.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = trailItem.author.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        PostLayoutContent(
                            contents = trailItem.contents,
                            layout = trailItem.layout,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // Main content (if repost, show reposter's content, else show post content)
        if (parent != null && root != null && !postFeed.contents.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp)
                ) {
                    AsyncImage(
                        model = postFeed.author.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = postFeed.author.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                PostLayoutContent(
                    contents = postFeed.contents ?: emptyList(),
                    layout = postFeed.layout
                )
            }
        } else {
            PostLayoutContent(
                contents = postFeed.contents,
                layout = postFeed.layout
            )
        }
    }
}

@Composable
private fun PostLayoutContent(
    contents: List<PostFeedContent>,
    layout: List<LayoutRoot>?,
    modifier: Modifier = Modifier
) {
    val layoutRoot = layout?.firstOrNull { it.type == "rows" }
    if (layoutRoot != null) {
        Column(modifier = modifier) {
            layoutRoot.display.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.blocks.forEach { blockIndex ->
                        val content = contents.getOrNull(blockIndex)
                        if (content != null) {
                            Box(modifier = Modifier.weight(1f)) {
                                RenderContent(content)
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Fallback: simple vertical list
        Column(modifier = modifier) {
            contents.sortedBy { it.order }.forEach { content ->
                RenderContent(content)
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
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
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

@Preview(name = "Normal Post", showBackground = true)
@Composable
fun PostFeedCardPreview() {
    val fakePost = createFakePost()
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PostFeedCard(
                postFeed = fakePost,
                onClickLike = {},
                onComment = {},
                onQuickRepost = {},
                onCommentRepost = { _ -> },
                onShare = {},
                onFollowClick = {}
            )
        }
    }
}

@Preview(name = "Liked Post", showBackground = true)
@Composable
fun PostFeedCardLikedPreview() {
    val fakePost = createFakePost(
        interactions = PostInteractions(liked = true, reposted = false, commented = false)
    )
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PostFeedCard(fakePost, {}, {}, {}, { _ -> }, {}, {})
        }
    }
}

@Preview(name = "With Trail", showBackground = true)
@Composable
fun PostFeedCardTrailPreview() {
    val root = PostParent(
        id = "root1",
        author = PostAuthor("rootAuth", "Original Author", ""),
        contents = listOf(PostFeedContent(id = 99, type = "text", order = 0, text = "Contenido original"))
    )
    val parent = PostParent(
        id = "parent1",
        author = PostAuthor("parentAuth", "Intermediate Reposter", "")
    )

    val trailItem = createFakePost(
        id = "trail1",
        author = PostAuthor("a2", "Trail Author", ""),
        contents = listOf(PostFeedContent(id = 10, type = "text", order = 0, text = "Este es un item del trail"))
    )
    val trailItem2 = createFakePost(
        id = "trail2",
        author = PostAuthor("a3", "Trail Author3", ""),
        contents = listOf(PostFeedContent(id = 11, type = "text", order = 1, text = "Este es un item del trail2"))
    )
    val fakePost = createFakePost(
        parent = parent,
        root = root,
        trail = listOf(trailItem, trailItem2)
    )
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PostFeedCard(fakePost, {}, {}, {}, { _ -> }, {}, {})
        }
    }
}

@Preview(name = "Same Parent and Root", showBackground = true)
@Composable
fun PostFeedCardSameParentRootPreview() {
    val rootAuthor = PostAuthor("rootAuth", "Original Author", "")
    val root = PostParent(
        id = "root1",
        author = rootAuthor,
        contents = listOf(PostFeedContent(id = 99, type = "text", order = 0, text = "Contenido original"))
    )
    val fakePost = createFakePost(
        parent = root,
        root = root
    )
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PostFeedCard(fakePost, {}, {}, {}, { _ -> }, {}, {})
        }
    }
}

@Preview(name = "Different Parent and Root", showBackground = true)
@Composable
fun PostFeedCardDifferentParentRootPreview() {
    val root = PostParent(
        id = "root1",
        author = PostAuthor("rootAuth", "Original Author", ""),
        contents = listOf(PostFeedContent(id = 99, type = "text", order = 0, text = "Contenido original"))
    )
    val parent = PostParent(
        id = "parent1",
        author = PostAuthor("parentAuth", "Intermediate Reposter", "")
    )
    val fakePost = createFakePost(
        parent = parent,
        root = root
    )
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PostFeedCard(fakePost, {}, {}, {}, { _ -> }, {}, {})
        }
    }
}

private fun createFakePost(
    id: String = "1",
    author: PostAuthor = PostAuthor("author1", "Aldair", "https://http.cat/images/400.jpg"),
    parent: PostParent? = null,
    root: PostParent? = null,
    trail: List<PostFeedItem> = emptyList(),
    interactions: PostInteractions? = PostInteractions(liked = false, reposted = false, commented = false),
    contents: List<PostFeedContent> = listOf(
        PostFeedContent(
            id = 1,
            type = "text",
            order = 0,
            text = "¡Hola! Este es un post de prueba para el feed de Loom.",
            media = null
        )
    )
): PostFeedItem {
    return PostFeedItem(
        id = id,
        author = author,
        parent = parent,
        root = root,
        trail = trail,
        status = "published",
        tags = listOf("android", "compose", "kotlin"),
        contents = contents,
        layout = listOf(
            LayoutRoot(
                type = "rows",
                display = listOf(LayoutRow(blocks = contents.indices.toList()))
            )
        ),
        interactions = interactions,
        stats = PostStats(likesCount = 10, repostsCount = 5, commentsCount = 2),
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now(),
        publishedAt = Clock.System.now()
    )
}
