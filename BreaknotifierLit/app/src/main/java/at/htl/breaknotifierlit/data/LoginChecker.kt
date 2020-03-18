package at.htl.breaknotifierlit.data

import android.content.Context
import android.util.Log
import at.htl.breaknotifierlit.MainActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Runnable

class LoginChecker(val id: String, val context: Context): Runnable {

    var res: Boolean = false
    private set

    override fun run() {
        val queue = Volley.newRequestQueue(context)
        val url = MainActivity.URL + "register/" + id

        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            res = true;
        }, Response.ErrorListener {
            res = false;
        })

        queue.add(stringRequest)
    }
}