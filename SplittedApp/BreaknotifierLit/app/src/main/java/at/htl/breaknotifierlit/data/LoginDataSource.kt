package at.htl.breaknotifierlit.data

import at.htl.breaknotifierlit.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource : Thread() {

    private var uname = ""
    private var pword = ""
    private var check = false
    private var user : LoggedInUser? = null;

    fun getUser(): LoggedInUser? {
        return user;
    }

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
            val cookie = Login.LoginToWebUntis(uname,pword)
            RegisterInServer.register(cookie,"TempID");
            if(cookie == null) {
                return Result.Error(IOException("Error logging in"));
            }
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "username")
            check = true;
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

