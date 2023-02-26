package net.fpoly.dailymart.utils

import android.content.Context

object SharedPref {

    private const val PREFERENCES_NAME = "daily_mart"

    fun getTimeOnApp(context: Context): Int {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPref.getInt("time_on_app", 0)
    }

    fun setTimeOnApp(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val num = getTimeOnApp(context) + 1
        editor.putInt("time_on_app", num).apply()
    }
}