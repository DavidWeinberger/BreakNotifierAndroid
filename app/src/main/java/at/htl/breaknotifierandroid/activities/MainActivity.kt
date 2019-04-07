package at.htl.breaknotifierandroid.activities

//import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import at.htl.breaknotifierandroid.R
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.data.LoginData
import at.htl.breaknotifierandroid.data.SharedPreferencesKeys
import at.htl.breaknotifierandroid.model.School
import kotlinx.android.synthetic.main.activity_main.*
import javax.ws.rs.core.NewCookie

class MainActivity : AppCompatActivity() {

    companion object {

        var static_cookie: NewCookie? = null
    }

    private val preferences: LoginData = LoginData.getInstance(this)
    internal val LOG_TAG = MainActivity::class.java.simpleName
    private val school : School = this.preferences.getSchool()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Break Notifier"

        bt_selectSchool.setOnClickListener {
            val intent = Intent(this, SchoolSelectionActivity::class.java)
            startActivity(intent)
        }

        bt_login.setOnClickListener {
            if(this.school.server == "" || this.school.displayName == "") {
                Toast.makeText(this@MainActivity, "Please select a school first", Toast.LENGTH_SHORT).show()

            } else {
                val username = this.et_username.text.toString()
                val password = this.et_password.text.toString()
                val backend = BackendJava()
                val cookie: NewCookie? = backend.login(school.server, school.displayName, username, password)

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

        school.displayName = SchoolSelectionActivity.selection.displayName
        school.displayName = SchoolSelectionActivity.selection.displayName
        tv_school.text = school.displayName
    }
}
