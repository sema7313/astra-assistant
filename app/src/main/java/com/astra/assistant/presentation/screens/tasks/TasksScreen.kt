package com.astra.assistant.presentation.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.astra.assistant.domain.model.Task
import com.astra.assistant.domain.model.TaskPriority
import com.astra.assistant.presentation.components.FloatingNavBar
import com.astra.assistant.presentation.components.GlassCard
import com.astra.assistant.presentation.theme.AccentCyan
import com.astra.assistant.presentation.theme.AccentGreen
import com.astra.assistant.presentation.theme.AccentPink
import com.astra.assistant.presentation.theme.DeepSpace
import com.astra.assistant.presentation.theme.TextLight

@Composable
fun TasksScreen(navController: NavHostController) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var currentRoute by remember { mutableStateOf("tasks") }

    fun getPriorityColor(priority: TaskPriority): Color = when (priority) {
        TaskPriority.HIGH -> Color(0xFFFF6B6B)
        TaskPriority.MEDIUM -> Color(0xFFFFD700)
        TaskPriority.LOW -> AccentGreen
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Задачи",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )
                IconButton(
                    onClick = {
                        tasks = tasks + Task(
                            id = System.currentTimeMillis().toString(),
                            title = "Новая задача",
                            description = "",
                            priority = TaskPriority.MEDIUM
                        )
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить",
                        tint = AccentGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Tasks List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks.size) { index ->
                    val task = tasks[index]
                    GlassCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = {
                                    tasks = tasks.map {
                                        if (it.id == task.id) it.copy(isCompleted = it.isCompleted.not())
                                        else it
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentGreen,
                                    uncheckedColor = AccentCyan
                                )
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    task.title,
                                    color = TextLight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                )
                                Text(
                                    task.description,
                                    color = TextLight.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = getPriorityColor(task.priority),
                                        shape = CircleShape
                                    )
                            )

                            IconButton(
                                onClick = { tasks = tasks.filter { it.id != task.id } },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Удалить",
                                    tint = AccentPink,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Navigation Bar
        FloatingNavBar(
            currentRoute = currentRoute,
            onNavigate = { route ->
                currentRoute = route
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
