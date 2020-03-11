package at.htl.breaknotifierlit.firebase

import android.app.Notification
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import at.htl.breaknotifierlit.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WebUntisFirebaseService: FirebaseMessagingService() {
    companion object {
        lateinit var notificationManager: NotificationManager
    }

    val TAG: String = "WebUntisFirebaseService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Message Received:")
        Log.i(TAG, " - Message Title: " + remoteMessage.data["title"])
        Log.i(TAG, " - Message Body: " + remoteMessage.data["body"])
        super.onMessageReceived(remoteMessage)

    }
}