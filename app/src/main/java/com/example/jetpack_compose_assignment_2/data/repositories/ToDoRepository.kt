package com.example.jetpack_compose_assignment_2.data.repositories

import com.example.jetpack_compose_assignment_2.data.api.ApiService
import com.example.jetpack_compose_assignment_2.data.local.dao.TodoDao
import com.example.jetpack_compose_assignment_2.data.local.entity.TodoEntity
import com.example.jetpack_compose_assignment_2.data.models.Todo
import com.example.jetpack_compose_assignment_2.utils.toEntity
import com.example.jetpack_compose_assignment_2.utils.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ToDoRepository @Inject constructor(
    private val apiService: ApiService,
    private val todoDao: TodoDao
) {
    suspend fun getTodos(): Flow<List<Todo>> = flow {
        val cached = todoDao.getAllTodos().map { it.toModel() }
        if (cached.isNotEmpty()) {
            emit(cached)
        }
        try {
            val fetched = apiService.getTodos()
            emit(fetched)
            fetched.forEach { todoDao.insertTodos(it.toEntity()) }
        } catch (e: Exception) {
                println("Failed to fetch todos: ${e.message}")
        }
    }


    suspend fun getTodoById(id: Int): Flow<Todo> = flow {
        val cached = todoDao.getTodoById(id)?.toModel()
        if (cached != null) {
            emit(cached)
        }
        try {
            val fetched = apiService.getTodoById(id)
            todoDao.insertTodos(fetched.toEntity())
            val updated = todoDao.getTodoById(id)?.toModel()
            if (updated != null) {
                emit(updated)
            }
        } catch (e: Exception) {
            println("Failed to fetch todo by id: ${e.message}")
        }
    }
}
