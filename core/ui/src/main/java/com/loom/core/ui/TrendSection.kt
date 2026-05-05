package com.loom.core.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.loom.core.model.data.TrendCategory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.TrendCategoryItem
import com.loom.core.model.data.TrendCategoryItemTag
import com.loom.core.model.data.TrendCategoryItemVideo
import com.loom.core.model.enum.TrendType

private const val TAG = "TrendSectionDebug"

@Composable
fun TrendSection(
    trendCategory: TrendCategory,
    modifier: Modifier = Modifier
) {
    when (trendCategory.subType ){
        TrendType.TAG -> {
            // 1. Usamos un Box como contenedor principal para las capas
            Box(modifier = modifier.fillMaxWidth().height(190.dp)) {

                // Capa 1: La imagen de fondo
                AsyncImage(imageUrl = trendCategory.iconUrl)

                // Capa 2: El degradado (Scrim)
                Box(
                    modifier = Modifier.matchParentSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.75f), // Más oscuro a la izquierda
                                    Color.Transparent // Transparente a la derecha
                                )
                            )
                        )
                )

                // Capa 3: El contenido real (Header + Content)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                ) {
                    TrendSectionHeader(trendCategory.category, trendCategory.count)
                    TrendSectionContent(trendCategory.items)
                }
            }
        }
        TrendType.VIDEO -> {
            Box(modifier = modifier.fillMaxWidth().height(190.dp)) {
                TrendSectionContent(trendCategory.items)
            }
        }
    }
}


@Composable
fun TrendSectionHeader(
    trendCategoryName: String,
    trendCount: String,
    modifier: Modifier = Modifier
){
    Log.d(TAG, "TrendSectionHeader: Rendering $trendCategoryName with count $trendCount")
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        // Aquí puedes poner tu ícono (AsyncImage o Icon)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray) // Placeholder
        ){
            AsyncImage(imageUrl = "https://http.cat/images/100.jpg")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = trendCategoryName
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = trendCount
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendSectionContent(
    trendCategoryItems: List<TrendCategoryItem>,
){
    Log.d(TAG, "TrendSectionContent: Received ${trendCategoryItems.size} items")

    HorizontalUncontainedCarousel(
        state = rememberCarouselState { trendCategoryItems.count() },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
            //.padding(top = 16.dp, bottom = 16.dp),
        itemWidth = 100.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        val item = trendCategoryItems[i]
        Log.d(TAG, "TrendSectionContent: Rendering item at index $i:") //${item.title}")
        TrendContentCard(
            item = item,
            //onClick = { /*TODO*/ }
        )
    }
}


@Preview(showBackground = true, name = "Explore Section")
@Composable
fun ExploreSectionPreview(
    @PreviewParameter(TrendSectionPreviewParameterProvider::class)
    trendCategory: TrendCategory
) {
    LoomTheme {
        TrendSection(
            trendCategory = trendCategory
        )

    }
}