package com.example.jetpack_compose_assignment_2.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.jetpack_compose_assignment_2.data.local.entity.TodoEntity

@Dao
interface TodoDao {
    @Upsert
    suspend fun insertTodos(todo: TodoEntity)

    @Query("SELECT * FROM todoentity")
    suspend fun getAllTodos(): List<TodoEntity>

    @Query("SELECT * FROM todoentity WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?
}