package com.example.jetpack_compose_assignment_2.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpack_compose_assignment_2.data.models.Todo
import com.example.jetpack_compose_assignment_2.ui.common.EmptyStateView
import com.example.jetpack_compose_assignment_2.ui.common.ErrorStateView
import com.example.jetpack_compose_assignment_2.ui.state.ListScreenEvent
import com.example.jetpack_compose_assignment_2.ui.viewmodels.ListScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    listScreenViewModel: ListScreenViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (Int) -> Unit
) {
    val state by listScreenViewModel.state.collectAsState()
    val patternColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My TODOs") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Canvas(modifier = Modifier.matchParentSize().alpha(0.5f)) {
                val step = 70.dp.toPx()
                for (x in 0..(size.width / step).toInt() + 1) {
                    for (y in 0..(size.height / step).toInt() + 1) {
                        drawCircle(
                            color = patternColor,
                            radius = 8.dp.toPx(), // Smaller circles
                            center = Offset(
                                x * step + if (y % 2 == 0) 0f else step / 2,
                                y * step
                            )
                        )
                    }
                }
            }

            when {
                // 1. Initial Loading (no data yet)
                state.isLoading && state.todoList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 3.dp)
                    }
                }
                // 2. Error and no cached data to show
                state.error != null && state.todoList.isEmpty() -> {
                    ErrorStateView(
                        modifier = Modifier.fillMaxSize(),
                        errorMessage = state.error ?: "An unexpected error occurred.",
                        onRetry = { listScreenViewModel.handleEvent(ListScreenEvent.RetryFetch) }
                    )
                }
                else -> {
                    if (state.todoList.isEmpty() && !state.isLoading) {
                        EmptyStateView(
                            modifier = Modifier.fillMaxSize(),
                            message = "Your TODO list is sparkling clean!"
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.todoList, key = { it.id }) { todo ->
                                TodoListItem(
                                    todo = todo,
                                    onClick = { onNavigateToDetailScreen(todo.id) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    if (state.error != null && state.todoList.isNotEmpty() && !state.isLoading) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(
                                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                        ) {
                            Text(
                                text = "Couldn't update list: ${state.error}",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = state.isLoading && state.todoList.isNotEmpty(),
                enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(300)),
                exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(300)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box( // Semi-transparent scrim for the loader
                    modifier = Modifier
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), CircleShape)
                        .padding(20.dp)
                ) {
                    CircularProgressIndicator(strokeWidth = 3.dp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListItem(
    todo: Todo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (todo.completed) "Status: Completed" else "Status: Pending",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (todo.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}