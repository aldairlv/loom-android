package com.loom.feature.comments.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.Comment
import com.loom.core.model.data.PostAuthor
import com.loom.core.ui.comment.CommentThread
import kotlinx.datetime.Clock

@Composable
fun CommentsScreen(
    postId: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommentsViewModel = hiltViewModel()
) {
    LaunchedEffect(postId) {
        viewModel.setPostId(postId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommentsScreen(
        uiState = uiState,
        onClose = onClose,
        onSendComment = viewModel::sendComment,
        onLoadMore = { viewModel.fetchComments(isLoadMore = true) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommentsScreen(
    uiState: CommentsUiState,
    onClose: () -> Unit,
    onSendComment: (String, String?) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var replyToComment by remember { mutableStateOf<Comment?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comentarios", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .windowInsetsPadding(WindowInsets.navigationBars) // Empuja el contenido hacia arriba
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (replyToComment != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Respondiendo a ${replyToComment?.author?.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { replyToComment = null }) {
                            Text("Cancelar", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe tu comentario...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        )
                    )
                    IconButton(
                        onClick = {
                            if (text.isNotBlank()) {
                                onSendComment(text, replyToComment?.id)
                                text = ""
                                replyToComment = null
                            }
                        },
                        enabled = text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = if (text.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (uiState) {
                is CommentsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CommentsUiState.Error -> {
                    Text("Error al cargar comentarios", modifier = Modifier.align(Alignment.Center))
                }
                is CommentsUiState.Success -> {
                    val listState = rememberLazyListState()
                    val shouldLoadMore by remember {
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            val totalItemsCount = layoutInfo.totalItemsCount
                            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                            lastVisibleItemIndex >= totalItemsCount - 3 && totalItemsCount > 0
                        }
                    }

                    LaunchedEffect(shouldLoadMore) {
                        if (shouldLoadMore) {
                            onLoadMore()
                        }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(uiState.comments, key = { it.id }) { comment ->
                            CommentThread(
                                comment = comment,
                                onReplyClick = { replyToComment = it }
                            )
                        }

                        if (uiState.isFetchingMore) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentsScreenPreview() {
    val fakeComments = listOf(
        Comment(
            id = "1",
            author = PostAuthor("user1", "Usuario 1", ""),
            text = "¡Qué gran post! Me encanta el contenido.",
            depth = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            isDeleted = false,
            replies = listOf(
                Comment(
                    id = "2",
                    author = PostAuthor("user2", "Usuario 2", ""),
                    text = "Totalmente de acuerdo.",
                    depth = 1,
                    createdAt = Clock.System.now(),
                    updatedAt = Clock.System.now(),
                    isDeleted = false
                )
            )
        ),
        Comment(
            id = "3",
            author = PostAuthor("user3", "Usuario 3", ""),
            text = "Interesante perspectiva, aunque no comparto todo.",
            depth = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            isDeleted = false
        )
    )

    LoomTheme {
        CommentsScreen(
            uiState = CommentsUiState.Success(comments = fakeComments),
            onClose = {},
            onSendComment = { _, _ -> },
            onLoadMore = {}
        )
    }
}
