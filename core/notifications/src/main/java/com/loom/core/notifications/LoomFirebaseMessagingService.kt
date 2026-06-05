package com.loom.core.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LoomFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Refreshed token: $token")
        // TODO: Enviar el token a tu servidor backend mediante un repositorio
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        // Verificar si el mensaje trae una notificación
        remoteMessage.notification?.let {
            Log.d("FCM_NOTIFICATION", "Message Notification Title: ${it.title}")
            Log.d("FCM_NOTIFICATION", "Message Notification Body: ${it.body}")
            // Aquí podrías disparar una notificación local si es necesario, 
            // aunque Firebase lo hace automáticamente si la app está en background.
        }
        
        // Verificar si trae datos (payload)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM_DATA", "Message data payload: ${remoteMessage.data}")
        }
    }
}
