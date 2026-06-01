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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loom.core.model.data.Comment
import kotlinx.coroutines.launch

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    postId: String,
    comments: List<Comment>,
    onDismissRequest: () -> Unit,
    onSendComment: (String, String?) -> Unit, // text, parentId
    isFetchingMore: Boolean = false,
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var showRealInput by remember { mutableStateOf(false) }
    var replyToComment by remember { mutableStateOf<Comment?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier.fillMaxHeight(0.9f), // A bit more than half
        dragHandle = null
    ) {
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
            Box(modifier = Modifier.weight(1f)) {
                val listState = rememberLazyListState()
                val shouldLoadMore = remember {
                    derivedStateOf {
                        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                        lastVisibleItemIndex >= listState.layoutInfo.totalItemsCount - 3
                    }
                }

                LaunchedEffect(shouldLoadMore.value) {
                    if (shouldLoadMore.value) {
                        onLoadMore()
                    }
                }

                val isAtTop by remember {
                    derivedStateOf { listState.firstVisibleItemIndex == 0 }
                }

                PullToRefreshBox(
                    state = rememberPullToRefreshState(),
                    isRefreshing = isFetchingMore && isAtTop,
                    onRefresh = onRefresh
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp) // Space for fake input
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
            }

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
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Use a Dialog or a full-screen overlay to catch clicks outside and handle IME
    // Or just a Box that sits at the bottom with imePadding
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
                .clickable(enabled = false) {} // Prevent dismissal when clicking the input area
                .imePadding()
                .padding(16.dp)
        ) {
            if (replyTo != null) {
                Text(
                    text = "Respondiendo a Usuario ${replyTo.profileId.take(5)}",
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
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = if (text.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
