package com.astra.assistant.presentation.screens.memory

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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.astra.assistant.domain.model.MemoryCategory
import com.astra.assistant.domain.model.MemoryItem
import com.astra.assistant.presentation.components.FloatingNavBar
import com.astra.assistant.presentation.components.GlassCard
import com.astra.assistant.presentation.theme.AccentCyan
import com.astra.assistant.presentation.theme.AccentGreen
import com.astra.assistant.presentation.theme.AccentPink
import com.astra.assistant.presentation.theme.DeepSpace
import com.astra.assistant.presentation.theme.TextLight

@Composable
fun MemoryScreen(navController: NavHostController) {
    var memories by remember { mutableStateOf(listOf<MemoryItem>()) }
    var selectedCategory by remember { mutableStateOf(MemoryCategory.ABOUT_ME) }
    var currentRoute by remember { mutableStateOf("memory") }

    val categories = listOf(
        MemoryCategory.ABOUT_ME,
        MemoryCategory.WORK,
        MemoryCategory.STUDY,
        MemoryCategory.PROJECTS,
        MemoryCategory.PREFERENCES,
        MemoryCategory.IMPORTANT_FACTS
    )

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
                    "Память",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextLight
                )
                IconButton(
                    onClick = { memories = memories + MemoryItem(
                        id = System.currentTimeMillis().toString(),
                        category = selectedCategory,
                        content = "Новая заметка"
                    ) },
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

            // Category Tabs
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    Text(
                        category.name.replace("_", " "),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { selectedCategory = category },
                        color = if (selectedCategory == category) AccentCyan else TextLight,
                        fontSize = 14.sp,
                        fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            // Memories List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(memories.filter { it.category == selectedCategory }.size) { index ->
                    val memory = memories.filter { it.category == selectedCategory }[index]
                    GlassCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                memory.content,
                                modifier = Modifier.weight(1f),
                                color = TextLight,
                                fontSize = 14.sp
                            )
                            IconButton(
                                onClick = { memories = memories.filter { it.id != memory.id } },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
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
