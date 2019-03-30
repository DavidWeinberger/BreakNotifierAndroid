package at.htl.breaknotifierandroid.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import at.htl.breaknotifierandroid.backend.BackendJava

class Timetable : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_timetable)
        val backend = BackendJava()
        backend.getDailyTimeTable(MainActivity.static_cookie)

    }
}
