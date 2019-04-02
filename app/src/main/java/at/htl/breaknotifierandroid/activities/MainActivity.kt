package at.htl.breaknotifierandroid.activities

//import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.R
import at.htl.breaknotifierandroid.activities.SchoolSelectionActivity.Companion.selection
import at.htl.breaknotifierandroid.backend.School
import kotlinx.android.synthetic.main.activity_main.*
import org.json.simple.JSONObject
import java.lang.Exception
import javax.ws.rs.core.NewCookie

class MainActivity : AppCompatActivity() {

    internal val LOG_TAG = MainActivity::class.java.simpleName
    private var school : School? = null

    companion object {

        var static_cookie: NewCookie? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Break Notifier"
        //selectedSchool.text = "BackendJava"

        bt_selectSchool.setOnClickListener {
            //Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            //val selectSchool = SchoolSelect()
            //selectSchool.start()
            val intent = Intent(this, SchoolSelectionActivity::class.java)
            startActivity(intent)
        }

        bt_login.setOnClickListener {
            if(this.school == null) {
                Toast.makeText(this@MainActivity, "Please select a school first", Toast.LENGTH_SHORT).show()

            } else {
                val username = this.et_username.text.toString()
                val password = this.et_password.text.toString()
                val backend = BackendJava()
                var cookie: NewCookie? = backend.login(school?.server, school?.displayName, username, password)

                if (cookie == null) {
                    Toast.makeText(this@MainActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(this@MainActivity, "The input was $username | $password", Toast.LENGTH_LONG).show()
                } else {
                    static_cookie = cookie
                    val intent = Intent(this, Timetable::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (SchoolSelectionActivity.selection != null) {
            school = SchoolSelectionActivity.selection
            tv_school.text = school?.displayName
        }
    }
}
