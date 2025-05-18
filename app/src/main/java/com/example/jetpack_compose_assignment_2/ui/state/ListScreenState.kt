package com.example.jetpack_compose_assignment_2.ui.state

import com.example.jetpack_compose_assignment_2.data.models.Todo

data class ListScreenState(
    val todoList: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ListScreenEvent {
    object GetTodos : ListScreenEvent() // Renamed for consistency
    object RetryFetch : ListScreenEvent()
}