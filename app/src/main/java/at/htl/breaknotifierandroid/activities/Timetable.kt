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
import at.htl.breaknotifierandroid.model.Lesson
import kotlinx.android.synthetic.main.activity_timetable.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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



        val backend = BackendJava()
        var lessons = backend.getDailyTimeTable(MainActivity.static_cookie)

        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, lessons)
        lv_lessons.adapter = adapter


        Thread {
            while (true) {
                this.check(lessons)
                Thread.sleep(30000)
            }
        }.start()

        this.bt_logout.setOnClickListener {
            LoginData.getInstance(this).resetData()
            this.onBackPressed()
        }

    }

    private fun check(lessons : MutableList<Lesson>){

        val channelID = "at.htl.Breaknotifier.notification"

        var temp : MutableList<Lesson> = mutableListOf()
        var count = 0
        for( i in lessons){
            val current = LocalDateTime.now();

            val formatter = DateTimeFormatter.ofPattern("HHmm")
            val formatted = current.format(formatter);
            if (i.endTime.toInt() <  formatted.toInt()){
                //lessons.removeAt(0)
                //lessons.remove(i)
                temp.add(i)
                count++
                //println(lessons.drop(0))
            }
        }
        if (temp.size > 0){
            var subject = temp.last()
            val notification = Notification.Builder(this,
                channelID)
                .setContentTitle("Stunde vorbei")
                .setContentText(subject.subjects + " ist vorbei!")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .build()


            notificationManager!!.notify(1, notification)
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
