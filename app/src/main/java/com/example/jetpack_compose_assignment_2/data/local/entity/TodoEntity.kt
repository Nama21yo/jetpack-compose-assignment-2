package com.example.jetpack_compose_assignment_2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoEntity(
    val userId: Int,
    val title: String,
    val completed: Boolean,
    @PrimaryKey
    val id: Int
)