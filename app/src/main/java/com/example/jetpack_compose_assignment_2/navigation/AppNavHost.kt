package com.example.jetpack_compose_assignment_2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpack_compose_assignment_2.ui.screen.DetailsScreen
import com.example.jetpack_compose_assignment_2.ui.screen.ListScreen
import com.example.jetpack_compose_assignment_2.ui.state.DetailScreenEvent
import com.example.jetpack_compose_assignment_2.ui.viewmodels.DetailScreenViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ListScreen.route
    ) {
        composable(Screen.ListScreen.route) {
            ListScreen(
                onNavigateToDetailScreen = { todoId ->
                    navController.navigate(Screen.DetailScreen.createRoute(todoId))
                }
            )
        }

        composable(
            route = Screen.DetailScreen.routeWithArg,
            arguments = listOf(navArgument(Screen.DetailScreen.ARG_TODO_ID) {
                type = NavType.IntType
                nullable = false
            })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt(Screen.DetailScreen.ARG_TODO_ID) ?: 0
            val viewModel: DetailScreenViewModel = hiltViewModel()

            LaunchedEffect(key1 = todoId) {
                if (todoId != 0) {
                    viewModel.handleEvent(DetailScreenEvent.OnIdChange(todoId))
                    viewModel.handleEvent(DetailScreenEvent.FetchTodo)
                } else {

                }
            }

            DetailsScreen(
                detailScreenViewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}