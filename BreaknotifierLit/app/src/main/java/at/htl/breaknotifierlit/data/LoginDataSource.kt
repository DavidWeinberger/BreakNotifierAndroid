package at.htl.breaknotifierlit.data

import at.htl.breaknotifierlit.MainActivity
import at.htl.breaknotifierlit.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource : Runnable {

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

    fun login(): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            RegisterInServer.register(uname,pword,MainActivity.token);

            /*val cookie = Login.LoginToWebUntis(uname,pword)
            if(cookie != null){
                RegisterInServer.register(uname,pword,MainActivity.token);
            }else {
                return Result.Error(IOException("Error logging in"));
            }*/
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), uname)
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

