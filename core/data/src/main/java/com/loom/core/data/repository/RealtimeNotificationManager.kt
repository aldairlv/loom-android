package com.loom.core.data.repository

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.loom.core.network.LoomNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealtimeNotificationManager @Inject constructor(
    private val notificationService: LoomNotificationService,
    private val userDataRepository: UserDataRepository,
) : DefaultLifecycleObserver {
    
    private var externalScope: CoroutineScope? = null
    private var connectionJob: Job? = null
    private var isStarted = false

    fun startListening(scope: CoroutineScope) {
        if (isStarted) return
        isStarted = true
        externalScope = scope
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        
        // Initial start if process is already started
        startSync()
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.d("RealtimeManager", "App in foreground, starting sync")
        startSync()
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d("RealtimeManager", "App in background, stopping sync")
        notificationService.disconnect()
        connectionJob?.cancel()
        connectionJob = null
    }

    private fun startSync() {
        if (connectionJob != null) return
        val scope = externalScope ?: return

        connectionJob = scope.launch {
            userDataRepository.userData
                .map { it.accessToken }
                .distinctUntilChanged()
                .collect { token ->
                    if (token != null) {
                        Log.d("RealtimeManager", "Token available, connecting WebSocket...")
                        notificationService.connect(token)
                    } else {
                        Log.d("RealtimeManager", "No token, disconnecting WebSocket...")
                        notificationService.disconnect()
                    }
                }
        }

        // Collect messages
        scope.launch {
            notificationService.messages.collect { message ->
                Log.d("RealtimeManager", "Realtime message received: $message")
                // TODO: Update local DB or trigger UI event
            }
        }
    }
}
