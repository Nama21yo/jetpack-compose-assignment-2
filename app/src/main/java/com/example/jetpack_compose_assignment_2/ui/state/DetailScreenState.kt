package com.example.jetpack_compose_assignment_2.ui.state

data class DetailScreenState(
    val id: Int = 0,
    val userId: Int = 0,
    val title: String = "",
    val completed: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val todoDataAvailable: Boolean = false // To distinguish no data vs. actual data
)

sealed class DetailScreenEvent {
    data class OnIdChange(val id: Int) : DetailScreenEvent()
    object FetchTodo : DetailScreenEvent()
    object RetryFetchTodo : DetailScreenEvent() // New event for retrying
}