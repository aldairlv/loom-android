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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.loom.core.model.data.Post


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
                    text = "Aldair",//post.authorName"",
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
            }

            // --- TAGS ---
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- INTERACCIONES (BOTTOM BAR) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Alcance/Interacción (Barritas)
                InteractionButton(iconRes = android.R.drawable.ic_menu_sort_by_size, count = "600")//post.reachCount)

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Comentario (Nube)
                    InteractionButton(
                        iconRes = android.R.drawable.stat_notify_chat,
                        count = "100",//post.commentCount.toString()
                    )                    // Reblog (Flechas)
                    InteractionButton(
                        iconRes = android.R.drawable.ic_menu_share,
                        count = "200",//post.reblogCount.toString(),
                        //onClick = onReblogClick
                    )                    // Like (Corazón)
                    InteractionButton(
                        iconRes = android.R.drawable.btn_star,
                        count = "300",//post.likeCount.toString(),
                        //onClick = onLikeClick
                    )                }
            }
        }
    }
}

@Composable
fun InteractionButton(iconRes: Int, count: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = count, color = Color.White, fontSize = 14.sp)
    }
}