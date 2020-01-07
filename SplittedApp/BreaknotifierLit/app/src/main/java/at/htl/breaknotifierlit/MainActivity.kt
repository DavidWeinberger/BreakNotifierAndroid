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
        if(true){
            //val schoolSelect = Intent(this, schoolSearch::class.java)
            //startActivity(schoolSelect);
            val login = Intent(this, webuntis_login::class.java)
            startActivity(login)
        }
    }
}
