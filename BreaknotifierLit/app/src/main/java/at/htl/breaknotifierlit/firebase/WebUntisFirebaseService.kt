package at.htl.breaknotifierlit.firebase

import android.app.Notification
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import at.htl.breaknotifierlit.MainActivity
import at.htl.breaknotifierlit.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class WebUntisFirebaseService: FirebaseMessagingService() {
    companion object {
        var checkDate = 0
    }

    val TAG: String = "WebUntisFirebaseService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Message Received:")
        Log.i(TAG, " - Message Title: " + remoteMessage.data["title"])
        Log.i(TAG, " - Message Body: " + remoteMessage.data["body"])
        super.onMessageReceived(remoteMessage)
        val date: LocalDate = LocalDate.now(ZoneId.of("CET"))
        var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        var formattedDate = date.format(formatter);
        var currentDate = formattedDate.toInt()

        println(checkDate.toString() + " | " + currentDate.toString())


        if(remoteMessage.data["title"].equals("#200")){
//            System.out.println(remoteMessage.data["body"])
            val subjects = JSONArray(remoteMessage.data["body"])
            for (i in 0 until subjects.length()){
                val item = subjects.getJSONObject(i)
                println(item)
                MainActivity.listItemsNew.add(item.getString("subject") + "\t\t\t\t" + item.getString("roomNr") + "\t\t\t" +  item.getString("className") + "\t\t\t"  + item.getString("teacher") + "\n" + item.getString("startTime") + " - " + item.getString("endTime"));
                if(i+1 < subjects.length()){
                    val end = item.getString("endTime").replace(":", "").toInt();
                    val start = subjects.getJSONObject(i + 1).getString("startTime").replace(":", "").toInt();
                    println("Time: " + (start-end))
                    if(start - end > 20){
                        MainActivity.listItemsNew.add("Pause \n" + item.getString("endTime") + " - " + subjects.getJSONObject(i + 1).getString("startTime") );
                    }
                }
            }
        } else if(remoteMessage.data["title"].equals("#100")) {
            MainActivity.uname = remoteMessage.data["body"]
        } else if(checkDate <= currentDate){
            val notification = Notification.Builder(this, getString(R.string.channel_id)).setContentTitle(remoteMessage.data["title"]).setContentText(remoteMessage.data["body"]).setSmallIcon(R.mipmap.ic_launcher).build()
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(0, notification)
            checkDate = 0
        }



    }
}