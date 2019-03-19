package at.htl.breaknotifierandroid.Activities

//import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import at.htl.breaknotifierandroid.Backend.Backend
import at.htl.breaknotifierandroid.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    internal val LOG_TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Break Notifier"
        //selectedSchool.text = "Test"

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
            //val backend = Backend.getInstance()

            //val loginSuccessful = backend.login(username, password)

            //Toast.makeText(this@MainActivity, if(loginSuccessful) "Login succeeded" else "Login failed", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this@MainActivity, "The input is $username | $password" , Toast.LENGTH_LONG).show()

        }
    }




    //val btn_select = findViewById(R.id.select) as Button


    // set on-click listener
    /*btn_select.setOnClickListener {
        Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
    }*/
}
