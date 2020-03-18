package at.htl.breaknotifierlit.data

import android.util.Log
import at.htl.breaknotifierlit.MainActivity
import at.htl.breaknotifierlit.ui.login.webuntis_login
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.lang.Exception
import java.util.concurrent.ExecutionException

class UserRegistration {

    companion object {
        fun register(uname: String, pw: String, id: String): Boolean {

            val ret = false

            Log.i("UserRegistration", "Entered register")

            val queue = Volley.newRequestQueue(webuntis_login.context)
            val url = MainActivity.URL + "register"

            val jsonObject = JSONObject()
            jsonObject.put("id", id)
            jsonObject.put("username", PasswordEncrypt.encrypt(uname, id))
            jsonObject.put("password", PasswordEncrypt.encrypt(pw, id))

            val future: RequestFuture<JSONObject> = RequestFuture.newFuture()
            val jsonRequest: JsonObjectRequest =
                JsonObjectRequest(Request.Method.POST, url, jsonObject, future, future)
            queue.add(jsonRequest)

            Log.i("UserRegistration", "Awaiting Response")
            try {
                // Calling this apparently blocks the current thread or something, so the response isn't processed.
                // The phone will still be registered (given that the user has typed the correct credentials) on the server.
                // Running the request in a Thread or Coroutine does not help, using a different Context (MainActivity for instance) has also proven futile.
                // If it weren't for this crap, it would run on SDK 23 just fine. Everything else seems to work.
                future.get()
                Log.i("UserRegistration", "Got Response")
            } catch(e: Exception) {
                Log.i("UserRegistration", "Got Error")
            }

            return ret
        }
    }
}