package at.htl.notificationapp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private var notificationManager: NotificationManager? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
            "at.htl.notificationapp.notification",
            "NotifyDemo",
            "Every Hour")


        press.setOnClickListener{
            var thread = SimpleThread(this)
            thread.start()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun change(changeValue: String){
        text_out_put.text = changeValue
        //fieldView.textView.text = changeValue
        println("$changeValue has run. ")

        val channelID = "at.htl.notificationapp.notification"

        val notification = Notification.Builder(this,
            channelID)
            .setContentTitle("Example Notification")
            .setContentText("This is an  example notification.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelID)
            .build()

        notificationManager!!.notify(1, notification)

    }
}

class SimpleThread(mainClass: MainActivity): Thread() {
    val input : MainActivity = mainClass

    @TargetApi(Build.VERSION_CODES.O)
    override fun run() {
        println("${Thread.currentThread()} has run.")
        var count = 0

        while(count < 1000){
            Thread.sleep(20_000)
            input.change(LocalDateTime.now().toString())
            count++

        }
    }
}
