package com.loom.core.ui
/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.loom.core.model.data.Carousel
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.loom.core.designsystem.theme.LoomTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import com.loom.core.designsystem.R.drawable
import com.loom.core.model.data.CarouselEventElement
import com.loom.core.model.data.CarouselUserElement
import com.loom.core.model.data.PostContent
import androidx.compose.material3.IconButton
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselCardExpanded(
    carousel: Carousel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(carousel) {
        println("DEBUG_CAROUSEL: Tipo=${carousel.type}, Cantidad=${carousel.elements.size}")
    }
    val carouselState = rememberCarouselState(itemCount = { carousel.elements.size })

    // En Uncontained, el itemWidth es LEY. No cambia según la pantalla.
    val itemWidth = if (carousel.type == "user_card") 200.dp else 310.dp
    val carouselHeight = if (carousel.type == "user_card") 280.dp else 360.dp

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalUncontainedCarousel(
            state = carouselState,
            itemWidth = itemWidth,
            itemSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(carouselHeight)
        ) { index ->
            val element = carousel.elements[index]
            LaunchedEffect(index) {
                println("DEBUG_CAROUSEL_ITEM: Dibujando index $index, TipoElemento=${element.objectType}")
            }
            // Aplicamos maskClip para que el redondeado sea consistente con M3
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                    //.maskClip(MaterialTheme.shapes.extraLarge),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                //shape = RoundedCornerShape(0.dp) // El redondeado lo da el maskClip
            ) {
                when (element) {
                    is CarouselUserElement -> UserCardContent(element)
                    is CarouselEventElement -> EventCardContent(element)
                    else -> {

                        Text("Tipo desconocido: ${element.objectType}", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCardContent(element: CarouselEventElement) {
    val event = element.resource.firstOrNull()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)) {
                AsyncImageBase(
                    imageUrl = event?.poster,
                    modifier = Modifier.fillMaxSize()
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                            endY = 100f
                        )
                    )
                )
            }


            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event?.title ?: "",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = event?.description ?: "",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                LoomActionButton(text = "Explorar")
            }
        }

        IconButton(
            onClick = { /* Close */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                .size(32.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun CarouselCardPreview(
    @PreviewParameter(CarouselUsersPreviewProvider::class)
    carousel: Carousel
) {
    LoomTheme {
        CarouselCardExpanded(
            carousel = carousel
        )
    }
}
*/
@Composable
fun UserCardContent(element: CarouselUserElement) {
    val user = element.resource.firstOrNull()
    LaunchedEffect(user) {
        println("DEBUG_UI: Usuario=${user?.username}, PostsCount=${user?.posts?.size ?: 0}")
        user?.posts?.firstOrNull()?.let { post ->
            println("DEBUG_UI: Primer Post ID=${post.id}, Bloques=${post.content.size}")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageBase(
                imageUrl = user?.avatar,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50)) // Circular
            )
            Text(
                text = user?.username ?: "Unknown",
                modifier = Modifier.padding(start = 10.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Imagen central con efecto de profundidad
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            val postImageUrl = user?.posts?.firstOrNull()?.content
                ?.filterIsInstance<PostContent.Image>()?.firstOrNull()?.imageUrl
            LaunchedEffect(postImageUrl) {
                println("DEBUG_UI: URL de imagen encontrada: $postImageUrl")
            }
            AsyncImageBase(
                imageUrl = postImageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Degradado inferior para que el nombre o botón resalte
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 300f
                        )
                    )
            )
        }

        LoomActionButton(
            text = "Seguir",
            modifier = Modifier.padding(12.dp)
        )
    }
}


@Composable
fun AsyncImageBase(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var isLoading by remember { mutableStateOf(true) }
    val isError = remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError.value = state is AsyncImagePainter.State.Error
        },
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)

        Image(
            painter = if (isError.value || LocalInspectionMode.current) painterResource(drawable.core_designsystem_ic_placeholder_default) else painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    }
}

@Composable
fun LoomActionButton(text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = { },
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3A)), // Gris claro
        shape = RoundedCornerShape(25.dp)
    ) {
        Text(text, color = Color.White)
    }
}

*/