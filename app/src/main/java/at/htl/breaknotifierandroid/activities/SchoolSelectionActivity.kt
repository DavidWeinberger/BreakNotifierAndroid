package at.htl.breaknotifierandroid.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.R
import kotlinx.android.synthetic.main.activity_school_selection.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.lang.Exception




class SchoolSelectionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_school_selection)
        //var schools = Backend.getListOfSchools("HTBLA Leond")

        var output : JSONObject? = null
        et_filter.setOnEditorActionListener{
            textView, keyCde, keyEvent ->
            var backend = BackendJava()
            try {
                output = backend.getSchools(textView.text.toString())
                var names = backend.schools(output)
                changer(names)
            }catch (e: Exception) {
                Toast.makeText(this, "Zu viele Ergebnisse",Toast.LENGTH_SHORT).show()
            }

            true
        }
        lv_schools.setOnItemClickListener { parent, view, position, id ->
            val object1 = output?.get("result") as JSONObject
            val object2 = object1["schools"] as JSONArray
            val obj = object2[id.toInt()] as JSONObject
            selection = obj
            onSupportNavigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public fun changer(names:List<String>){
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        lv_schools.adapter = adapter;
    }


    companion object {
        lateinit var selection: JSONObject
    }
}
