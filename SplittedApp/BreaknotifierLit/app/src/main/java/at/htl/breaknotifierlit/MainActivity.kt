package at.htl.breaknotifierlit

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Secure
import androidx.appcompat.app.AppCompatActivity
import at.htl.breaknotifierlit.ui.login.webuntis_login


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val deviceID = Secure.getString(contentResolver,
            Secure.ANDROID_ID)
        println(deviceID)
        /*
        Start School search activity
         */

        //Check if logged in, otherwise start Login activity
        if(true){
            val login = Intent(this, webuntis_login::class.java)
            startActivity(login)
        }
    }
}
