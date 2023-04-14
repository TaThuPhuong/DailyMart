package net.fpoly.dailymart.utils

import android.content.Context
import com.google.gson.Gson
import net.fpoly.dailymart.data.model.BankInfo
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.security.AESUtils

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
        return Gson().fromJson(result, User::class.java) ?: User()
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

    fun getTokenNotification(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString("token_notification", "")!!
    }

    fun setTokenNotification(context: Context, value: String) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString("token_notification", value).apply()
    }

    fun setAccessToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        sharedPref.putString("token", token).apply()
    }

    fun getAccessToken(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return AESUtils.decrypt(sharedPref.getString("token", "")!!)
    }

    fun setBankInfo(context: Context, bankInfo: BankInfo) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        val gson = Gson().toJson(bankInfo)
        sharedPref.putString("bank_info", gson).apply()
    }

    fun getBankInfo(context: Context): BankInfo {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val result = sharedPref.getString("bank_info", "")
        return Gson().fromJson(result, BankInfo::class.java) ?: BankInfo()
    }
}