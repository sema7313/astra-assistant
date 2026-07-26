package com.astra.assistant.presentation.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.astra.assistant.domain.model.ChatMessage
import com.astra.assistant.presentation.components.FloatingNavBar
import com.astra.assistant.presentation.components.GlassCard
import com.astra.assistant.presentation.theme.AccentCyan
import com.astra.assistant.presentation.theme.DeepSpace
import com.astra.assistant.presentation.theme.GlassDark
import com.astra.assistant.presentation.theme.SurfaceDark
import com.astra.assistant.presentation.theme.TextLight

@Composable
fun ChatScreen(navController: NavHostController) {
    var messages by remember {
        mutableStateOf(listOf<ChatMessage>())
    }
    var inputText by remember { mutableStateOf("") }
    var currentRoute by remember { mutableStateOf("chat") }

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
                "Чат",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight
            )

            // Messages List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            Text(
                                message.content,
                                modifier = Modifier.padding(12.dp),
                                color = TextLight,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Input Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = GlassDark,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    placeholder = { Text("Напишите сообщение...", color = TextLight.copy(alpha = 0.5f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = GlassDark,
                        unfocusedContainerColor = GlassDark,
                        focusedTextColor = TextLight,
                        unfocusedTextColor = TextLight,
                        cursorColor = AccentCyan
                    )
                )
                IconButton(
                    onClick = {
                        if (inputText.isNotEmpty()) {
                            messages = messages + ChatMessage(
                                id = System.currentTimeMillis().toString(),
                                isUser = true,
                                content = inputText,
                                timestamp = System.currentTimeMillis()
                            )
                            inputText = ""
                        }
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Отправить",
                        tint = AccentCyan
                    )
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
