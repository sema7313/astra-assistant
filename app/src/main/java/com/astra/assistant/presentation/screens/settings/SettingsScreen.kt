package com.astra.assistant.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.astra.assistant.presentation.components.FloatingNavBar
import com.astra.assistant.presentation.components.GlassCard
import com.astra.assistant.presentation.theme.AccentCyan
import com.astra.assistant.presentation.theme.AccentGreen
import com.astra.assistant.presentation.theme.DeepSpace
import com.astra.assistant.presentation.theme.TextLight

@Composable
fun SettingsScreen(navController: NavHostController) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(true) }
    var currentRoute by remember { mutableStateOf("settings") }
    var subscriptionTier by remember { mutableStateOf("FREE") }

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
            Text(
                "Настройки",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Subscription Section
                item {
                    Text(
                        "Подписка",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan
                    )
                }

                items(3) { index ->
                    val tier = when (index) {
                        0 -> "FREE"
                        1 -> "PRO"
                        else -> "PRO MAX"
                    }
                    val features = when (tier) {
                        "FREE" -> "Базовый AI, Чат, Память"
                        "PRO" -> "AI Code + Trading, Неограниченно"
                        else -> "Все + Voice 24/7, Приоритет"
                    }

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { subscriptionTier = tier }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    tier,
                                    color = TextLight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    features,
                                    color = TextLight.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                            if (subscriptionTier == tier) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Активна",
                                    tint = AccentGreen
                                )
                            }
                        }
                    }
                }

                // Settings Section
                item {
                    Text(
                        "Параметры",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                // Notifications
                item {
                    GlassCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Уведомления",
                                color = TextLight,
                                fontSize = 14.sp
                            )
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AccentCyan,
                                    checkedTrackColor = AccentCyan.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }

                // Dark Mode
                item {
                    GlassCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Тёмная тема",
                                color = TextLight,
                                fontSize = 14.sp
                            )
                            Switch(
                                checked = darkMode,
                                onCheckedChange = { darkMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AccentCyan,
                                    checkedTrackColor = AccentCyan.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }

                // About
                item {
                    GlassCard {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                "О приложении",
                                color = TextLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Астра v1.0.0",
                                color = TextLight.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                "AI Assistant with Privacy First",
                                color = TextLight.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
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
