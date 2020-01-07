package at.htl.breaknotifierlit.data

import at.htl.breaknotifierlit.data.model.LoggedInUser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.FormBody
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource : Thread() {

    private var uname = ""
    private var pword = ""
    private var check = false

    fun check() : Boolean{
        return check;
    }

    fun init(username: String, password: String){
        uname = username
        pword = password
    }

    public override fun run() {
        println("Started login Thread......")
        login()
    }

    private fun login(): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val url = URL("https://mese.webuntis.com/WebUntis/j_spring_security_check")
            val builder = FormBody.Builder()
            val client = OkHttpClient()
            builder.add("school","htbla linz leonding")
            builder.add("j_username", uname)
            builder.add("j_password", pword)
            builder.add("token" , "")
            var request = Request.Builder().url(url).post(builder.build()).build()

            val call = client.newCall(request)
            val response = call.execute()
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "username")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

