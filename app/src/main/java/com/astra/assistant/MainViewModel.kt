package com.astra.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astra.assistant.domain.model.ChatMessage
import com.astra.assistant.domain.model.MemoryCategory
import com.astra.assistant.domain.model.MemoryItem
import com.astra.assistant.domain.model.OrbState
import com.astra.assistant.domain.model.SubscriptionTier
import com.astra.assistant.domain.model.Task
import com.astra.assistant.domain.model.TaskPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _orbState = MutableStateFlow<OrbState>(OrbState.IDLE)
    val orbState: StateFlow<OrbState> = _orbState.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _memoryItems = MutableStateFlow<List<MemoryItem>>(emptyList())
    val memoryItems: StateFlow<List<MemoryItem>> = _memoryItems.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _subscriptionTier = MutableStateFlow(SubscriptionTier.FREE)
    val subscriptionTier: StateFlow<SubscriptionTier> = _subscriptionTier.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Load from local database
        }
    }

    fun startListening() {
        viewModelScope.launch {
            _orbState.value = OrbState.LISTENING
        }
    }

    fun stopListening() {
        viewModelScope.launch {
            _orbState.value = OrbState.IDLE
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            // Add user message
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                isUser = true,
                content = text,
                timestamp = System.currentTimeMillis()
            )
            _chatMessages.value = _chatMessages.value + userMessage

            // Simulate AI thinking
            _orbState.value = OrbState.THINKING
            kotlinx.coroutines.delay(1500)

            // Add AI response
            val aiMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                isUser = false,
                content = "Это демо-ответ. API не подключен.",
                timestamp = System.currentTimeMillis()
            )
            _chatMessages.value = _chatMessages.value + aiMessage
            _orbState.value = OrbState.IDLE
        }
    }

    fun addMemoryItem(item: MemoryItem) {
        viewModelScope.launch {
            _memoryItems.value = _memoryItems.value + item
        }
    }

    fun deleteMemoryItem(item: MemoryItem) {
        viewModelScope.launch {
            _memoryItems.value = _memoryItems.value.filter { it.id != item.id }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value + task
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.map {
                if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _tasks.value = _tasks.value.filter { it.id != task.id }
        }
    }

    fun upgradeSubscription(tier: SubscriptionTier) {
        viewModelScope.launch {
            _subscriptionTier.value = tier
        }
    }
}
