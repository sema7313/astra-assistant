package com.astra.assistant

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.astra.assistant.service.AssistantForegroundService
import com.astra.assistant.ui.navigation.AstraNavGraph
import com.astra.assistant.ui.theme.AstraTheme
import com.astra.assistant.utils.PermissionsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        // Даже если пользователь отклонил часть разрешений, UI продолжает работать —
        // просто голосовые функции, требующие микрофон, будут молчать, пока не разрешит вручную.
        if (grants[android.Manifest.permission.RECORD_AUDIO] == true) {
            startAssistantService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNeededPermissions()
        requestIgnoreBatteryOptimizations()

        setContent {
            AstraTheme {
                AstraNavGraph()
            }
        }
    }

    /** Запрашивает микрофон и (на Android 13+) уведомления — без них ассистент не сможет работать. */
    private fun requestNeededPermissions() {
        val missing = PermissionsHelper.missingPermissions(this)
        if (missing.isNotEmpty()) {
            permissionLauncher.launch(missing.toTypedArray())
        } else {
            startAssistantService()
        }
    }

    private fun startAssistantService() {
        val intent = Intent(this, AssistantForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent)
        } else {
            startService(intent)
        }
    }

    /**
     * Просит пользователя исключить приложение из оптимизации батареи — без этого
     * система на многих устройствах (особенно Xiaomi/Huawei/Samsung) будет убивать
     * фоновую службу ассистента через некоторое время, и он "выключится" сам по себе.
     */
    @Suppress("BatteryLife")
    private fun requestIgnoreBatteryOptimizations() {
        val powerManager = getSystemService(android.os.PowerManager::class.java)
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = android.net.Uri.parse("package:$packageName")
                }
                startActivity(intent)
            } catch (e: Exception) {
                // Некоторые прошивки (особенно кастомные) не поддерживают этот intent напрямую —
                // не роняем приложение, просто пропускаем этот шаг.
            }
        }
    }
}
