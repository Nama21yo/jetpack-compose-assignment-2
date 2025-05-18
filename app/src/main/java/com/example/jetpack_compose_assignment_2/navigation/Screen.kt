package com.example.jetpack_compose_assignment_2.navigation

sealed class Screen(val route: String) {
    object ListScreen : Screen("todo_list_screen")
    object DetailScreen : Screen("todo_detail_screen/{todoId}") {
        fun createRoute(todoId: Int) = "todo_detail_screen/$todoId"

        const val ARG_TODO_ID = "todoId" // Argument key
        val routeWithArg = "todo_detail_screen/{$ARG_TODO_ID}"
    }
}