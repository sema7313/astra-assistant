package com.astra.assistant.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.astra.assistant.presentation.screens.chat.ChatScreen
import com.astra.assistant.presentation.screens.home.HomeScreen
import com.astra.assistant.presentation.screens.memory.MemoryScreen
import com.astra.assistant.presentation.screens.settings.SettingsScreen
import com.astra.assistant.presentation.screens.tasks.TasksScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("chat") {
            ChatScreen(navController)
        }
        composable("memory") {
            MemoryScreen(navController)
        }
        composable("tasks") {
            TasksScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
    }
}
