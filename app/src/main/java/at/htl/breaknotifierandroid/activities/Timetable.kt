package at.htl.breaknotifierandroid.activities

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.data.LoginData
import at.htl.breaknotifierandroid.model.Lesson
import at.htl.breaknotifierandroid.model.TimetableAdapter
import kotlinx.android.synthetic.main.activity_timetable.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import at.htl.breaknotifierandroid.R as projectR

class Timetable : AppCompatActivity() {
    private var adapter : TimetableAdapter? = null
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
        var lessons: ArrayList<Lesson> = backend.getDailyTimeTable(MainActivity.static_cookie) as ArrayList<Lesson>

        adapter = TimetableAdapter(this, lessons, true)
        lv_lessons.adapter = adapter


        Thread {
            while (true) {
                var deleting = this.check(lessons)
                if(deleting > 0){
                    for (x in 0 until deleting){
                        lessons.removeAt(0);
                    }
                    //updateUI();
                }

                Thread.sleep(30000)
            }
        }.start()

        this.bt_logout.setOnClickListener {
            LoginData.getInstance(this).resetData()
            this.onBackPressed()
        }

        this.bt_joinLessons.setOnClickListener {
            (this.lv_lessons.adapter as TimetableAdapter).joinSelectedLessons()
            lessons =  backend.getDailyTimeTable(MainActivity.static_cookie) as ArrayList<Lesson>
            (this.lv_lessons.adapter as TimetableAdapter).changeItems(lessons)
        }

        this.bt_unjoinLessons.setOnClickListener {
            lessons =  backend.getDailyTimeTable(MainActivity.static_cookie) as ArrayList<Lesson>
            (this.lv_lessons.adapter as TimetableAdapter).changeItems(lessons)
            (this.lv_lessons.adapter as TimetableAdapter).resetSelection()
        }
        }

    private fun updateUI(){
        adapter?.notifyDataSetChanged();
    }

    private fun check(lessons : MutableList<Lesson>): Int {

        val channelID = "at.htl.Breaknotifier.notification"
        var formatted = "";
        var temp : MutableList<Lesson> = mutableListOf()
        var count = 0
        for( l in lessons){
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("HHmm")
            formatted = current.format(formatter)
            if (l.endTime.toInt() <= formatted.toInt()){
                //lessons.removeAt(0)
                //lessons.remove(l)
                temp.add(l)
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
        else if(lessons.first().startTime.toInt()-3 == formatted.toInt()){
            val notification = Notification.Builder(this,
                channelID)
                .setContentTitle("Nächste Stunde")
                .setContentText(lessons.first().subjects+ " im Raum: " + lessons.first().room)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .build()

            notificationManager!!.notify(2, notification)
        }
        return temp.size;
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        val importance = NotificationManager.IMPORTANCE_HIGH
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
