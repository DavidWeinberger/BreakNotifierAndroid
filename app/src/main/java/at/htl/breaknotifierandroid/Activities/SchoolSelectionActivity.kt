package at.htl.breaknotifierandroid.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import at.htl.breaknotifierandroid.Backend.Backend
import at.htl.breaknotifierandroid.Backend.Test
import at.htl.breaknotifierandroid.R
import javax.json.JsonArray
import javax.json.JsonObject

class SchoolSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_selection)
        //var schools = Backend.getListOfSchools("HTBLA Leond")
        var test = Test()
        var output = test.testing()
        System.out.println(output)
    }
}
