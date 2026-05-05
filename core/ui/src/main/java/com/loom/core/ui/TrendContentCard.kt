package com.loom.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.PostContent
import com.loom.core.model.data.TrendCategoryItem
import com.loom.core.model.data.TrendCategoryItemTag
import com.loom.core.model.data.TrendCategoryItemVideo


@Composable
fun TrendContentCard(
    item: TrendCategoryItem,
    //onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    when (item){
        is TrendCategoryItemVideo -> {
            val post =  item.resource.firstOrNull()
            val videoContent = post?.content?.filterIsInstance<PostContent.Video>()?.firstOrNull()
            val videoUrl = videoContent?.videoUrl

            Card(
                modifier = Modifier
                    .width(140.dp),
                    //.aspectRatio(0.75f), //  altura automática según ancho
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                    // modifier = Modifier.fillMaxSize()
                    //modifier = Modifier.weight(1f) // ocupa solo el espacio disponible
                ){
                    AsyncVideo(
                        videoUrl = videoUrl,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
        is TrendCategoryItemTag -> {
            val post =  item.resource.firstOrNull()
            val imageContent = post?.content?.filterIsInstance<PostContent.Image>()?.firstOrNull()
            val imageUrl = imageContent?.imageUrl

            Card(
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(0.75f), //  altura automática según ancho
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Row(
                    Modifier.fillMaxWidth().height(20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seguir",
                        color = Color(0xFF00B8FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        lineHeight = 10.sp, // Igualar esto al fontSize
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            //.align(Alignment.CenterVertically)
                            //.clickable { onFollowClick() }
                            //.padding(horizontal = 4.dp)
                    )
                }
                Box(
                    //modifier = Modifier.fillMaxSize()
                    modifier = Modifier
                        .weight(1f) // ocupa solo el espacio disponible
                ){
                    AsyncImage(imageUrl = imageUrl)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(color = Color(0x801A1A1A)), // 0x80 = 50% de transparencia
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.tag.name,
                            color = Color(0xFFFFFFFF),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }


}

@Preview(showBackground = true, name = "Trend Content Card Tag")
@Composable
fun TrendContentCardTagPreview(
    @PreviewParameter(TrendContentCardTagPreviewParameterProvider::class)
    trendContentTag: TrendCategoryItem
)
{
    LoomTheme{
        TrendContentCard(
            item = trendContentTag
        )
    }
}



@Preview(showBackground = true, name = "Trend Content Card Video")
@Composable
fun TrendContentCardVideoPreview(
    @PreviewParameter(TrendContentCardVideoPreviewParameterProvider::class)
    trendContentVideo: TrendCategoryItem
)
{
    LoomTheme{
        TrendContentCard(
            item = trendContentVideo
        )
    }
}