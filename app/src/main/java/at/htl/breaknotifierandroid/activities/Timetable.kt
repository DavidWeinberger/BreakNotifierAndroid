package at.htl.breaknotifierandroid.activities

import android.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.data.LoginData
import kotlinx.android.synthetic.main.activity_timetable.*
import at.htl.breaknotifierandroid.R as projectR

class Timetable : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(projectR.layout.activity_timetable)
        val backend = BackendJava()
        val lessons = backend.getDailyTimeTable(MainActivity.static_cookie)
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, lessons)
        this.lv_lessons.adapter = adapter

        this.bt_logout.setOnClickListener {
            LoginData.getInstance(this).resetData()
            this.onBackPressed()
        }
    }
}
