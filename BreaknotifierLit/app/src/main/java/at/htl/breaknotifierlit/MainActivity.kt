package at.htl.breaknotifierlit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import at.htl.breaknotifierlit.ui.login.webuntis_login
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var IP: String
        var token: String? = ""
    }





    override fun onCreate(savedInstanceState: Bundle?) {

        MainActivity.IP = getString(R.string.server_ip)


        val channel = NotificationChannel(getString(R.string.channel_id), getString(R.string.channel_name), NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

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
