package at.htl.breaknotifierandroid.data

import android.content.Context
import android.content.SharedPreferences
import at.htl.breaknotifierandroid.model.School

class LoginData private constructor() {

    private lateinit var context: Context
    private lateinit var preferences: SharedPreferences

    private fun setContext(context: Context) {
        this.context = context
        this.preferences = this.context.getSharedPreferences(SharedPreferencesKeys.ID.key, Context.MODE_PRIVATE)
    }

    fun saveLoginData(schoolName: String, schoolUrl: String, username: String, password: String){
        val editor = this.preferences.edit()
        editor.putString(SharedPreferencesKeys.SCHOOL_NAME.key, schoolName)
        editor.putString(SharedPreferencesKeys.SCHOOL_URL.key, schoolUrl)
        editor.putString(SharedPreferencesKeys.USERNAME.key, username)
        editor.putString(SharedPreferencesKeys.PASSWORD.key, password)
        editor.apply()
    }

    fun resetData() {
        val editor = this.preferences.edit()
        editor.putString(SharedPreferencesKeys.SCHOOL_NAME.key, "")
        editor.putString(SharedPreferencesKeys.SCHOOL_URL.key, "")
        editor.putString(SharedPreferencesKeys.USERNAME.key, "")
        editor.putString(SharedPreferencesKeys.PASSWORD.key, "")
        editor.apply()
    }

    fun getSchool(): School {
        return School(this.preferences.getString(SharedPreferencesKeys.SCHOOL_NAME.key, ""), this.preferences.getString(SharedPreferencesKeys.SCHOOL_URL.key, ""))
    }

    fun getUserData(): Pair<String, String> {
        return Pair(this.preferences.getString(SharedPreferencesKeys.USERNAME.key, ""), this.preferences.getString(SharedPreferencesKeys.PASSWORD.key, ""))
    }

    companion object {
        private val instance: LoginData by lazy { LoginData() }

        fun getInstance(context: Context): LoginData {
            instance.setContext(context)
            return instance
        }
    }
}

enum class SharedPreferencesKeys(val key: String) {
    ID("loginData"),
    SCHOOL_NAME("schoolDisplayName"),
    SCHOOL_URL("schoolUrl"),
    USERNAME("username"),
    PASSWORD("password")
}