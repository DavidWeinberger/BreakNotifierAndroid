package at.htl.breaknotifierandroid.activities

//import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import at.htl.breaknotifierandroid.backend.BackendJava
import at.htl.breaknotifierandroid.R
import kotlinx.android.synthetic.main.activity_main.*
import org.json.simple.JSONObject
import java.lang.Exception
import javax.ws.rs.core.NewCookie

class MainActivity : AppCompatActivity() {
    internal val LOG_TAG = MainActivity::class.java.simpleName
    private lateinit var school : JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Break Notifier"
        //selectedSchool.text = "BackendJava"

        bt_selectSchool.setOnClickListener{
            //Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            //val selectSchool = SchoolSelect()
            //selectSchool.start()
            val intent = Intent(this, SchoolSelectionActivity::class.java)
            startActivity(intent)

        }



        bt_login.setOnClickListener{

            val username = this.et_username.text.toString()
            val password = this.et_password.text.toString()
            val backend = BackendJava()
            var cookie: NewCookie? =backend.login(school.get("server").toString()
                , school.get("loginName").toString(), username, password)
            if(cookie == null){
                Toast.makeText(this@MainActivity, "Passwort/Username falsch.", Toast.LENGTH_SHORT).show()
            }
            else{
                static_cookie = cookie
                val intent = Intent(this, Timetable::class.java)
                startActivity(intent)
            }
            //val backend = Backend.getInstance()

            //val loginSuccessful = backend.login(username, password)

            //Toast.makeText(this@MainActivity, if(loginSuccessful) "Login succeeded" else "Login failed", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this@MainActivity, "The input is $username | $password" , Toast.LENGTH_LONG).show()

        }
    }

    companion object {
        lateinit var static_cookie: NewCookie
    }

    override fun onResume() {
        super.onResume()
        try {
            if (!SchoolSelectionActivity.selection.isEmpty()){
                var obj = SchoolSelectionActivity.selection
                school = obj
                tv_school.text = obj.get("displayName").toString()
            }
        }catch (e : Exception)
        {
            System.out.println("Schule wurde noch nicht ausgewählt");
        }
    }

}
