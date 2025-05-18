package com.example.jetpack_compose_assignment_2.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpack_compose_assignment_2.ui.common.ErrorStateView
import com.example.jetpack_compose_assignment_2.ui.state.DetailScreenEvent
import com.example.jetpack_compose_assignment_2.ui.state.DetailScreenState
import com.example.jetpack_compose_assignment_2.ui.viewmodels.DetailScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    detailScreenViewModel: DetailScreenViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by detailScreenViewModel.state.collectAsState()
    val patternColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TODO Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
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
                            radius = 8.dp.toPx(),
                            center = Offset(
                                x * step + if (y % 2 == 0) 0f else step / 2,
                                y * step
                            )
                        )
                    }
                }
            }

            when {
                state.isLoading && !state.todoDataAvailable -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 3.dp)
                    }
                }
                state.error != null && !state.todoDataAvailable -> {
                    ErrorStateView(
                        modifier = Modifier.fillMaxSize(),
                        errorMessage = state.error ?: "Failed to load details.",
                        onRetry = { detailScreenViewModel.handleEvent(DetailScreenEvent.RetryFetchTodo) }
                    )
                }
                state.todoDataAvailable -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        DetailCard(state = state) // Extracted card content
                        Spacer(modifier = Modifier.height(24.dp))
                        Button( // Place button outside the card for better separation
                            onClick = onBack,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                        ) {
                            Text("Close")
                        }
                    }

                    if (state.error != null && !state.isLoading) {
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
                                text = "Couldn't update details: ${state.error}",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                !state.isLoading && state.error == null && !state.todoDataAvailable -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Select a TODO to see details.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.isLoading && state.todoDataAvailable,
                enter = fadeIn(animationSpec = androidx.compose.animation.core.tween(300)),
                exit = fadeOut(animationSpec = androidx.compose.animation.core.tween(300)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
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

@Composable
private fun DetailCard(state: DetailScreenState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItem(label = "Title", value = state.title)
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            DetailItem(label = "User ID", value = state.userId.toString())
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            DetailItem(label = "Item ID", value = state.id.toString())
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            DetailItem(
                label = "Status",
                value = if (state.completed) "Completed" else "Pending",
                valueColor = if (state.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, valueColor: Color = LocalContentColor.current) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = valueColor
        )
    }
}