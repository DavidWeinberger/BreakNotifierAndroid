package at.htl.breaknotifierlit.firebase

import android.app.Notification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.htl.breaknotifierlit.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WebUntisFirebaseService: FirebaseMessagingService() {
    val TAG: String = "WebUntisFirebaseService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Message Received:")
        Log.i(TAG, " - Message Title: " + remoteMessage.data["title"])
        Log.i(TAG, " - Message Body: " + remoteMessage.data["body"])
        super.onMessageReceived(remoteMessage)
        val notification: Notification = NotificationCompat.Builder(this).setContentTitle(remoteMessage.data["title"]).setContentText(remoteMessage.data["body"]).setSmallIcon(
            R.mipmap.ic_launcher).build()
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        manager.notify(123, notification)
    }
}