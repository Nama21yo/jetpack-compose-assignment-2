package com.example.jetpack_compose_assignment_2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.repositories.ToDoRepository
import com.example.jetpack_compose_assignment_2.ui.state.DetailScreenEvent
import com.example.jetpack_compose_assignment_2.ui.state.DetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DetailScreenState())
    val state: StateFlow<DetailScreenState> = _state

    // init {} // Fetching is now primarily driven by AppNavHost's LaunchedEffect

    fun handleEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.OnIdChange -> {
                _state.value = _state.value.copy(id = event.id, todoDataAvailable = false, error = null)
                // AppNavHost will typically send FetchTodo after this
            }
            is DetailScreenEvent.FetchTodo -> fetchTodo()
            is DetailScreenEvent.RetryFetchTodo -> fetchTodo() // Retry calls fetchTodo
        }
    }

    private fun fetchTodo() {
        val todoId = state.value.id
        if (todoId == 0) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Invalid Todo ID. Cannot fetch details.",
                todoDataAvailable = false
            )
            return
        }

        _state.value = _state.value.copy(isLoading = true, error = null) // Keep existing data if any

        viewModelScope.launch {
            toDoRepository.getTodoById(todoId)
                .catch { e -> // Catch errors from the Flow itself
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "An unknown error occurred while fetching details.",
                        // todoDataAvailable might still be true if cached data was emitted before error
                    )
                }
                .collect { todo ->
                    _state.value = _state.value.copy(
                        id = todo.id,
                        userId = todo.userId,
                        title = todo.title,
                        completed = todo.completed,
                        isLoading = false,
                        error = null,
                        todoDataAvailable = true
                    )
                }
        }
    }
}