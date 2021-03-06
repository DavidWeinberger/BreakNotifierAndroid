package at.htl.breaknotifierlit.data

import at.htl.breaknotifierlit.data.model.LoggedInUser
import kotlin.concurrent.thread

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Boolean {
        // handle login
        dataSource.init(username, password)

        //val result = dataSource.login()
        val thread =  Thread(dataSource)
        thread.start()
        thread.join()
        if(dataSource.check()){
            dataSource.getUser()?.let { setLoggedInUser(it) }
            return true
        }
        return false
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
