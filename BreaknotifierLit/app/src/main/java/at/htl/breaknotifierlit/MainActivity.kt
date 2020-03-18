package at.htl.breaknotifierlit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import at.htl.breaknotifierlit.ui.login.webuntis_login
import com.google.android.gms.tasks.OnCompleteListener


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var URL: String
        var token: String? = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        //School Server
        MainActivity.URL = "http://vm109.htl-leonding.ac.at/"
        //Own Server
        //MainActivity.IP = "http://134.255.233.103:80/"
        //Localhost
        //MainActivity.IP = "http://172.17.216.54:13131/"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Token", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                MainActivity.token = token;
            })

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(true){
            val login = Intent(this, webuntis_login::class.java)

            startActivity(login)

        }
    }
}
