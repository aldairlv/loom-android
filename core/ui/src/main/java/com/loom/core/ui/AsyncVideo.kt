package com.loom.core.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.loom.core.designsystem.R.drawable

@OptIn(UnstableApi::class)
@Composable
fun AsyncVideo(
    videoUrl: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isLocalInspection = LocalInspectionMode.current

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            try {
                val mediaItem = MediaItem.fromUri(videoUrl ?: "")
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            } catch (e: Exception) {
                isError = true
            }
        }
    }

    // Listener para estados (igual que AsyncImage)
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                isLoading = state == Player.STATE_BUFFERING
            }

            override fun onPlayerError(error: PlaybackException) {
                isError = true
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(), //.fillMaxWidth(),//.height(250.dp),
        contentAlignment = Alignment.Center
    ) {

        // 🔄 Loader (igual que tu imagen)
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        // ❌ Error fallback (igual filosofía que placeholder)
        if (isError || isLocalInspection) {
            Image(
                painter = painterResource(drawable.core_designsystem_ic_placeholder_default),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false

                        resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            /*AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false // estilo feed
                    }
                },
                modifier = Modifier.fillMaxSize()
            )*/
        }
    }
}