package com.example.jetpack_compose_assignment_2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetpack_compose_assignment_2.data.local.dao.TodoDao
import com.example.jetpack_compose_assignment_2.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1
)
abstract class ToDoDatabase: RoomDatabase() {
    abstract val todoDao: TodoDao
}