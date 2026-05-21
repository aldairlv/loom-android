package com.loom.core.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostParent
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

            // Contents
            postFeed.contents.sortedBy { it.order }.forEach { content ->
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
                        content.media?.url?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                    //.clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
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
                IconButton(onClick = onComment, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment")
                }
                IconButton(onClick = onRepost, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repost")
                }
                IconButton(onClick = onClickLike, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
                }
                IconButton(onClick = onShare, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
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
