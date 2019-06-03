package at.htl.breaknotifierandroid.activities

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.data.LoginData
import kotlinx.android.synthetic.main.activity_timetable.*
import at.htl.breaknotifierandroid.R as projectR

class Timetable : AppCompatActivity() {
    private var notificationManager: NotificationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(projectR.layout.activity_timetable)

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
            "at.htl.Breaknotifier.notification",
            "BreakNotifier",
            "Every Hour")

        val channelID = "at.htl.Breaknotifier.notification"

        val notification = Notification.Builder(this,
            channelID)
            .setContentTitle("Stunde endet")
            .setContentText("Diese Unterrichtseinheit ist vorbei!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelID)
            .build()

        notificationManager!!.notify(1, notification)

        val backend = BackendJava()
        val lessons = backend.getDailyTimeTable(MainActivity.static_cookie)


        
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, lessons)
        this.lv_lessons.adapter = adapter

        this.bt_logout.setOnClickListener {
            LoginData.getInstance(this).resetData()
            this.onBackPressed()
        }


    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        notificationManager?.createNotificationChannel(channel)
    }


}
