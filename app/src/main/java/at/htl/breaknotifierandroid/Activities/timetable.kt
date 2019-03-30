package at.htl.breaknotifierandroid.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import at.htl.breaknotifierandroid.Backend.Backend
import at.htl.breaknotifierandroid.Backend.BackendJava
import at.htl.breaknotifierandroid.R

class timetable : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)
        val backend = BackendJava()
        backend.getDailyTimeTable(MainActivity.static_cookie)

    }
}
