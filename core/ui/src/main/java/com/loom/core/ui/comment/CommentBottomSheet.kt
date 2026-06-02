package com.loom.core.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loom.core.model.data.Comment
import com.loom.core.model.data.PostAuthor
import androidx.compose.ui.tooling.preview.Preview
import com.loom.core.designsystem.theme.LoomTheme
import kotlinx.datetime.Clock
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    postId: String,
    comments: List<Comment>,
    onDismissRequest: () -> Unit,
    onSendComment: (String, String?) -> Unit, // text, parentId
    isFetchingMore: Boolean = false,
    onLoadMore: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier.fillMaxHeight(0.75f),
        dragHandle = null
    ) {
        CommentBottomSheetContent(
            comments = comments,
            onDismissRequest = onDismissRequest,
            onSendComment = onSendComment,
            isFetchingMore = isFetchingMore,
            onLoadMore = onLoadMore
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheetContent(
    comments: List<Comment>,
    onDismissRequest: () -> Unit,
    onSendComment: (String, String?) -> Unit,
    isFetchingMore: Boolean = false,
    onLoadMore: () -> Unit = {},
) {
    var showRealInput by remember { mutableStateOf(false) }
    var replyToComment by remember { mutableStateOf<Comment?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Comentarios",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }

            // Comments List
            Box(modifier = Modifier.weight(1f).background(Color.Gray)) {
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
                    //contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(comments, key = { it.id }) { comment ->
                        CommentThread(
                            comment = comment,
                            onReplyClick = { 
                                replyToComment = it
                                showRealInput = true
                            }
                        )
                    }

                    if (isFetchingMore && listState.firstVisibleItemIndex != 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
            Row(){
            // Fake Input Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showRealInput = true }
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Añade un comentario...",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        }

        if (showRealInput) {
            RealCommentInput(
                replyTo = replyToComment,
                onDismiss = { 
                    showRealInput = false
                    replyToComment = null
                },
                onSend = { text ->
                    onSendComment(text, replyToComment?.id)
                    showRealInput = false
                    replyToComment = null
                }
            )
        }
    }
}

@Composable
fun CommentThread(
    comment: Comment,
    onReplyClick: (Comment) -> Unit,
    depth: Int = 0
) {
    Column {
        CommentItem(
            comment = comment,
            onReplyClick = onReplyClick,
            depth = depth
        )
        comment.replies.forEach { reply ->
            CommentThread(
                comment = reply,
                onReplyClick = onReplyClick,
                depth = depth + 1
            )
        }
    }
}

@Composable
fun RealCommentInput(
    replyTo: Comment?,
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val isPreview = LocalInspectionMode.current

    LaunchedEffect(Unit) {
        if (!isPreview) {
            focusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clickable(enabled = false) {}
                .imePadding()
                .padding(16.dp)
        ) {
            if (replyTo != null) {
                Text(
                    text = "Respondiendo a Usuario ${replyTo.author.displayName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = { Text("Escribe tu comentario...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                )
                IconButton(
                    onClick = { if (text.isNotBlank()) onSend(text) },
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
    }
}

@Preview(showBackground = true)
@Composable
fun CommentBottomSheetPreview() {
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
        Surface(modifier = Modifier.fillMaxSize()) {
            CommentBottomSheetContent(
                comments = fakeComments,
                onDismissRequest = {},
                onSendComment = { _, _ -> }
            )
        }
    }
}
