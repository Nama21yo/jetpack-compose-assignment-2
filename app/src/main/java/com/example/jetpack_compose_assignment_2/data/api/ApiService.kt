package com.example.jetpack_compose_assignment_2.data.api

import com.example.jetpack_compose_assignment_2.data.models.Todo
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/todos")
    suspend fun getTodos(): List<Todo>

    @GET("/todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): Todo
}