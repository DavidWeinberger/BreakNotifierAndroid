package at.htl.breaknotifierandroid

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_select_school.*
import javax.json.JsonObject

class SelectSchool : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_school)
        supportActionBar!!.title = "Select School"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var strings = arrayOf("test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9")

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, strings)

        lv_schools.adapter = adapter

        lv_schools.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this@SelectSchool, strings[position] , Toast.LENGTH_SHORT).show()
        }


        et_input.setOnClickListener{
            Toast.makeText(this@SelectSchool, "Test" , Toast.LENGTH_SHORT).show()
            val backend: Backend = Backend.getInstance()
            var jsonObject: JsonObject = backend.getSchoolQueryResults("Htbla Linz")
            println(jsonObject)
        }

        /*tv_school_1.setOnClickListener {
            if (tv_school_1.text == "1"){
                Toast.makeText(this@SelectSchool, "TextView1 pressed" , Toast.LENGTH_SHORT).show()
                tv_school_1.text = "gedrückt"
            }
        }
        tv_school_2.setOnClickListener {
            Toast.makeText(this@SelectSchool, "TextView2 pressed" , Toast.LENGTH_SHORT).show()
        }
        tv_school_3.setOnClickListener {
            Toast.makeText(this@SelectSchool, "TextView3 pressed" , Toast.LENGTH_SHORT).show()
        }
        tv_school_4.setOnClickListener {
            Toast.makeText(this@SelectSchool, "TextView4 pressed" , Toast.LENGTH_SHORT).show()
        }
        tv_school_5.setOnClickListener {
            Toast.makeText(this@SelectSchool, "TextView5 pressed" , Toast.LENGTH_SHORT).show()
        }*/

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
