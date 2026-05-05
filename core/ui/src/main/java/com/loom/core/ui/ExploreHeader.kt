package com.loom.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.loom.core.designsystem.R.drawable
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.Post
import com.loom.core.ui.AsyncImage

@Composable
fun ExploreHeroHeader(
    imageUrl: String,
    //searchQuery: String,
    //onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth().height(250.dp)) {
        AsyncImage(imageUrl)

        // Contenedor para la barra de búsqueda anclada abajo
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            /*ExploreSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange
            )*/
        }
    }
}




@Preview(showBackground = true, name = "Explore con Datos")
@Composable
fun ExploreHeroHeaderPreview(
    @PreviewParameter(PostCardPreviewParameterProvider::class)
    post: Post
) {
    LoomTheme {
        ExploreHeroHeader(
            imageUrl = "https://http.cat/images/100.jpg"
        )
    }
}