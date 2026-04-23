package com.loom.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.loom.core.designsystem.component.LoomIconToggleButton
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.model.data.Post
import com.loom.core.model.data.PostContent
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.loom.core.designsystem.R.drawable
import com.loom.core.designsystem.theme.LoomTheme
import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun PostCardExpanded(
    post: Post,
    //onFollowClick: () -> Unit,
    //onMenuClick: () -> Unit,
    //onTagClick: (String) -> Unit,
    //onViewAllTagsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A) // Gris oscuro estilo Tumblr
        )
    ) {
        val isLiked = false // Estado ficticio para el corazón
        val onToggleLike = { /*TODO*/ }
        val isCommented = false // Estado ficticio para la nube
        val onToggleComment = { /*TODO*/ }
        val isReposted = false // Estado ficticio para las flechas
        val onToggleRepost = { /*TODO*/ }
        val isInteracted = false
        val onToggleInteracted = { /*TODO*/ }
        val likesCount = post.likesCount
        val commentsCount = post.commentsCount
        val repostsCount = post.reposts_count
        val notesCount = post.notesCount


        Column(modifier = Modifier.padding(vertical = 12.dp)) {

            // --- PARTE SUPERIOR: Usuario y Acciones ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar circular
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray) // Placeholder
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Nombre de usuario (con ellipsis si es largo)
                Text(
                    text = "AldairBlog",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Botón Seguir (si no lo sigues)
                if (true) {//!post.isFollowed
                    Text(
                        text = "Seguir",
                        color = Color(0xFF00B8FF), // Azul Tumblr
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            //.clickable { onFollowClick() }
                            .padding(horizontal = 8.dp)
                    )
                }

                IconButton(onClick = { /*TODO*/ }){//onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Opciones",
                        tint = Color.LightGray
                    )
                }
            }

            // --- CONTENIDO DEL POST ---
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth()
            ) {
                //postContent()
                if (post.contentBlocks.isNotEmpty()) {
                    PostContentList(post.contentBlocks)
                }
            }

            // --- TAGS ---
            // --- TAGS ---
            if (post.tags.isNotEmpty()) {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ){
                    PostTags(
                        tags = post.tags,
                        onTagClick = { tag -> println("Click en $tag") },
                        onViewAllClick = { /* Abrir pantalla de tags o expandir */ }
                    )

                }

            }
            /*Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                // Flujo de texto para los tags
                Text(
                    text = "#tag1",//post.tags.joinToString(" ") { "#$it" },
                    color = Color.Gray,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                // Botón "Ver todas" si los tags son muchos (simplificado)
                Text(
                    text = "Ver todas",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        //.clickable { onViewAllTagsClick() }
                )
            }*/

            Spacer(modifier = Modifier.height(16.dp))

            // --- INTERACCIONES (BOTTOM BAR) ---
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InteractionButton(isInteracted,notesCount, onToggleInteracted)
                CommentButton(isCommented, commentsCount, onToggleComment)
                RepostButton(isReposted, repostsCount, onToggleRepost)
                LikeButton(isLiked, likesCount,onToggleLike)
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostTags(
    tags: List<String>,
    onTagClick: (String) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Usamos ContextualFlowRow para manejar el desbordamiento (maxLines = 3)
    ContextualFlowRow(
        modifier = modifier.fillMaxWidth(),
        itemCount = tags.size,
        maxLines = 3, // Límite de 3 filas
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        overflow = ContextualFlowRowOverflow.expandIndicator {
            // Este es el "Ver todas" que aparece al final de la 3ra fila
            Text(
                text = "... Ver todas",
                color = Color.LightGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .padding(vertical = 4.dp)
            )
        }
    ) { index ->
        val tag = tags[index]
        TagItem(tag = tag, onClick = { onTagClick(tag) })
    }
}

@Composable
fun TagItem(
    tag: String,
    onClick: () -> Unit
) {
    // Aquí implementamos el texto con el '#' delante
    // Para el efecto de "subrayado", en móviles solemos usar la respuesta táctil
    // Pero si quieres subrayado visual programático:
    Text(
        text = "#$tag",
        color = Color.Gray,
        fontSize = 14.sp,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Eliminamos el ripple gris por defecto si queremos algo más limpio
            ) { onClick() }
            .padding(vertical = 2.dp)
    )
}

@Composable
fun PostContentList(
    contentBlocks: List<PostContent>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        contentBlocks.forEach { block ->
            when (block) {
                is PostContent.Text -> {
                    Text(
                        text = block.text,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                is PostContent.Image -> {
                    // Aquí usarías una librería como Coil para cargar la URL
                    AsyncImage(imageUrl = block.imageUrl)

                    // Placeholder visual:
                    /*Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray)
                    ) {
                        Text("Imagen: ${block.width}x${block.height}", Modifier.align(Alignment.Center))
                    }*/
                }
            }
        }
    }
}

@Composable
fun AsyncImage(
    imageUrl: String?,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            // Display a progress bar while loading
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && !isLocalInspection) {
                imageLoader
            } else {
                painterResource(drawable.core_designsystem_ic_placeholder_default)
            },
            // TODO b/226661685: Investigate using alt text of  image to populate content description
            // decorative image,
            contentDescription = null,
        )
    }
}



@Composable
fun LikeButton(
    isBookmarked: Boolean,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        LoomIconToggleButton(
            checked = isBookmarked,
            onCheckedChange = { onClick() },
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = LoomIcons.LikeBorder,
                    contentDescription = "Like",
                    tint = Color.Gray
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = LoomIcons.Like,
                    contentDescription = "Like",
                    tint = Color.Red
                )
            },
        )
        if (count > 0) {
            Text(
                text = count.toString(),
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}



@Composable
fun CommentButton(
    isCommented: Boolean,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        LoomIconToggleButton(
            checked = isCommented,
            onCheckedChange = { onClick() },
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = LoomIcons.CommentBorder,
                    contentDescription = "Comment",
                    tint = Color.Gray
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = LoomIcons.Comment,
                    contentDescription = "Comment",
                    tint = Color.Blue
                )
            },
        )
        if (count > 0) {
            Text(
                text = count.toString(),
                color = Color.LightGray,
                fontSize = 14.sp,
            )
        }

    }
}


@Composable
fun RepostButton(
    isReposted: Boolean,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        LoomIconToggleButton(
            checked = isReposted,
            onCheckedChange = { onClick() },
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = LoomIcons.RepostBorder,
                    contentDescription = "Repost",
                    tint = Color.Gray
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = LoomIcons.Repost,
                    contentDescription = "Repost",
                    tint = Color.Green
                )
            },
        )
        if (count > 0) {
            Text(
                text = count.toString(),
                color = Color.LightGray,
                fontSize = 14.sp,
            )
        }

    }
}


@Composable
fun InteractionButton(
    isInteracted: Boolean,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        LoomIconToggleButton(
            checked = isInteracted,
            onCheckedChange = { onClick() },
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = LoomIcons.InteractionBorder,
                    contentDescription = "Interaction",
                    tint = Color.Gray
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = LoomIcons.Interaction,
                    contentDescription = "Interaction",
                    tint = Color.Yellow
                )
            },
        )
        if (count > 0) {
            Text(
                text = count.toString(),
                color = Color.LightGray,
                fontSize = 14.sp,
            )
        }

    }
}


@Preview(showBackground = true, name = "Feed con Datos")
@Composable
fun PostCardPreview(
    @PreviewParameter(PostCardPreviewParameterProvider::class)
    post: Post
) {
    LoomTheme {
        PostCardExpanded(
            post = post
        )
    }
}