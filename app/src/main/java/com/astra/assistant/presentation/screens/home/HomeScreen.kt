package com.astra.assistant.presentation.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.astra.assistant.domain.model.OrbState
import com.astra.assistant.presentation.components.AnimatedOrb
import com.astra.assistant.presentation.components.FloatingNavBar
import com.astra.assistant.presentation.components.GlassCard
import com.astra.assistant.presentation.theme.AccentCyan
import com.astra.assistant.presentation.theme.AccentGreen
import com.astra.assistant.presentation.theme.AccentPink
import com.astra.assistant.presentation.theme.DeepSpace
import com.astra.assistant.presentation.theme.SurfaceDark
import com.astra.assistant.presentation.theme.TextLight

data class QuickAction(
    val title: String,
    val description: String,
    val icon: String
)

@Composable
fun HomeScreen(navController: NavHostController) {
    var orbState by remember { mutableStateOf(OrbState.IDLE) }
    var currentRoute by remember { mutableStateOf("home") }

    val quickActions = listOf(
        QuickAction("AI Code Helper", "Помощь с кодом", "💻"),
        QuickAction("AI Trading", "Анализ рынка", "📈"),
        QuickAction("Идеи", "Генерация идей", "💡"),
        QuickAction("Перевод", "Перевод текста", "🌐"),
        QuickAction("Резюме", "Написание резюме", "📄"),
        QuickAction("Email", "Написание писем", "📧"),
        QuickAction("Анализ", "Анализ данных", "📊"),
        QuickAction("Медитация", "Релаксация", "🧘")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Header
            Text(
                "Астра",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight
            )

            // Animated Orb
            Box(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedOrb(state = orbState)
            }

            // Status Text
            Text(
                when (orbState) {
                    OrbState.IDLE -> "Готова к работе"
                    OrbState.LISTENING -> "Слушаю..."
                    OrbState.THINKING -> "Думаю..."
                    OrbState.SPEAKING -> "Говорю..."
                },
                fontSize = 16.sp,
                color = AccentCyan,
                fontWeight = FontWeight.Medium
            )

            // Quick Actions Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quickActions.size) { index ->
                    val action = quickActions[index]
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                orbState = OrbState.THINKING
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                action.icon,
                                fontSize = 32.sp
                            )
                            Text(
                                action.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLight,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                action.description,
                                fontSize = 10.sp,
                                color = TextLight.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Bottom Controls
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 90.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { orbState = OrbState.LISTENING },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = AccentCyan.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Микрофон",
                    tint = AccentCyan,
                    modifier = Modifier.size(24.dp)
                )
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
