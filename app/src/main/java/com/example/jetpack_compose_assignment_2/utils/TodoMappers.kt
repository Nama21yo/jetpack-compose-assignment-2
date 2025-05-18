package com.example.jetpack_compose_assignment_2.utils

import com.example.jetpack_compose_assignment_2.data.local.entity.TodoEntity
import com.example.jetpack_compose_assignment_2.data.models.Todo

fun TodoEntity.toModel(): Todo {
    return Todo(
        userId = this.userId,
        id = this.id,
        title = this.title,
        completed = this.completed
    )
}

fun Todo.toEntity(): TodoEntity {
    return TodoEntity(
        userId = this.userId,
        id = this.id,
        title = this.title,
        completed = this.completed
    )
}