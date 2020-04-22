package at.htl.breaknotifierlit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import at.htl.breaknotifierlit.firebase.WebUntisFirebaseService
import at.htl.breaknotifierlit.ui.login.webuntis_login
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.WebTarget


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var IP: String
        var token: String? = ""
        var uname: String? = ""
        var listItemsNew = ArrayList<String>()
    }


    var listItems = ArrayList<String>()

    var adapter: ArrayAdapter<String>? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        MainActivity.IP = getString(R.string.server_ip)

        val channel = NotificationChannel(getString(R.string.channel_id), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(0, 400, 100, 300, 100, 50)

        val uri = Uri.parse("android.resource://"+this.packageName + "/ClockSound.wav")

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val att = AudioAttributes.Builder().
                setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build();

        channel.setSound(uri,att)

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
        val login = Intent(this, webuntis_login::class.java)

        startActivity(login)

        val listView = findViewById<ListView>(R.id.lv_subjects)


        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listItems
        )
        listView.adapter = adapter;


        Thread(Runnable {
            while(true){
                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    if(listItemsNew.size != listItems.size){
                        if(listItemsNew.size < listItems.size){
                            for (items in listItems){
                                if(!listItemsNew.contains(items)){
                                    adapter!!.remove(items)
                                    //listItems.remove(items)
                                }
                            }
                        } else if(listItemsNew.size > listItems.size){
                            for (items in listItemsNew){
                                if(!listItems.contains(items)){
                                    adapter!!.add(items)
                                    println(listItems.size)
                                    //listItems.add(items)
                                }
                            }
                        }
                    }

                    if(uname != ""){
                        val tv = findViewById<TextView>(R.id.Status)
                        tv.text = "Verbunden " + uname
                    } else {
                        val tv = findViewById<TextView>(R.id.Status)
                        tv.text = "Nicht Verbunden"
                    }

                    if(WebUntisFirebaseService.checkDate == 0){
                        findViewById<Button>(R.id.bt_mute).text = "Benachrichtigungen \n " +
                                "für einen Tag \nDeaktivieren"
                    } else {
                        findViewById<Button>(R.id.bt_mute).text = "Benachrichtigungen \nAktivieren"
                    }
                })
                Thread.sleep(500)
            }

        }).start()

        findViewById<Button>(R.id.bt_logout).setOnClickListener {
            Thread(Runnable {
                val client = ClientBuilder.newClient()
                try {
                    val target: WebTarget
                    val serverUrl = IP + "register/logout/" + token
                    target = client.target(serverUrl)
                    val response = target.request().delete()


                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    client.close()
                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        val login = Intent(this, webuntis_login::class.java)

                        startActivity(login)
                    })
                }
            }).start()
        }

        findViewById<Button>(R.id.bt_mute).setOnClickListener {
            if(WebUntisFirebaseService.checkDate == 0){
                val date: LocalDate = LocalDate.now(ZoneId.of("CET"))
                var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                var formattedDate = date.format(formatter);
                println(formattedDate);
                WebUntisFirebaseService.checkDate = formattedDate.toInt() + 1
                findViewById<Button>(R.id.bt_mute).text = "Benachrichtigungen \n " +
                        "für einen Tag \nDeaktivieren"
            } else {
                WebUntisFirebaseService.checkDate = 0
                findViewById<Button>(R.id.bt_mute).text = "Benachrichtigungen \nAktivieren"
            }
        }


    }
}
