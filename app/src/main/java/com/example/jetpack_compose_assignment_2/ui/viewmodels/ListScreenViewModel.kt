package com.example.jetpack_compose_assignment_2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.repositories.ToDoRepository
import com.example.jetpack_compose_assignment_2.ui.state.ListScreenEvent
import com.example.jetpack_compose_assignment_2.ui.state.ListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ListScreenState())
    val state: StateFlow<ListScreenState> = _state

    init {
        handleEvent(ListScreenEvent.GetTodos)
    }

    fun handleEvent(event: ListScreenEvent) {
        when (event) {
            is ListScreenEvent.GetTodos -> fetchTodos()
            is ListScreenEvent.RetryFetch -> fetchTodos()
        }
    }

    private fun fetchTodos() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                toDoRepository.getTodos().collectLatest { todoList ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        todoList = todoList,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
}