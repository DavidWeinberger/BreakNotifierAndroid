package at.htl.breaknotifierandroid.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import at.htl.breaknotifierandroid.R
import kotlin.concurrent.thread

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Thread {
            start()
        }.start()
        supportActionBar?.hide()
    }

    private fun start(){
        Thread.sleep(3000)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
