package net.fpoly.dailymart.utils

import android.content.Context
import com.google.gson.Gson
import net.fpoly.dailymart.data.model.User

object SharedPref {

    private const val PREFERENCES_NAME = "daily_mart"

    fun getTimeOnApp(context: Context): Int {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getInt("time_on_app", 0)
    }

    fun setTimeOnApp(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        val num = getTimeOnApp(context) + 1
        sharedPref.putInt("time_on_app", num).apply()
    }

    fun insertUser(context: Context, user: User) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        val gson = Gson().toJson(user)
        sharedPref.putString("user", gson).apply()
    }

    fun getUser(context: Context): User {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val result = sharedPref.getString("user", "")
        return Gson().fromJson(result, User::class.java)
    }

    fun getNotificationId(context: Context): Int {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getInt("notification_id", 0)
    }

    fun setNotificationId(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        val num = getNotificationId(context) + 1
        sharedPref.putInt("notification_id", num).apply()
    }

    fun getTokenNotification(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString("token", "")
    }

    fun setTokenNotification(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString("token", value).apply()
    }
}