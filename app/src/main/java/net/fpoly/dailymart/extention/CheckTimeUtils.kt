package net.fpoly.dailymart.extention

import android.text.format.DateUtils
import java.text.DecimalFormat
import java.util.*

object CheckTimeUtils {
    @JvmField
    var mDecimalFormat = DecimalFormat("00")
    fun getStringTime(timeMilLiSeconds: Long): String {
        val minute = (timeMilLiSeconds / 1000L / 60).toInt()
        val second = (timeMilLiSeconds / 1000L % 60).toInt()
        return mDecimalFormat.format(minute.toLong()) + ":" + mDecimalFormat.format(second.toLong())
    }

    @JvmStatic
    fun getStringTime(timeSeconds: Int): String {
        val minute = timeSeconds / 60
        val second = timeSeconds % 60
        return mDecimalFormat.format(minute.toLong()) + ":" + mDecimalFormat.format(second.toLong())
    }

    @JvmStatic
    fun isToday(time: Long): Boolean {
        return DateUtils.isToday(time)
    }

    @JvmStatic
    fun isYesterday(time: Long): Boolean {
        return DateUtils.isToday(time + DateUtils.DAY_IN_MILLIS)
    }

    @JvmStatic
    fun isDayBeforeYesterday(time: Long): Boolean {
        return DateUtils.isToday(time + 2 * DateUtils.DAY_IN_MILLIS)
    }

    @JvmStatic
    fun isLastYesterday(time: Long): Boolean {
        return time < Calendar.getInstance().timeInMillis && !DateUtils.isToday(time) && !DateUtils.isToday(
            time + DateUtils.DAY_IN_MILLIS,
        )
    }

    @JvmStatic
    fun daysBetween(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar[Calendar.HOUR] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return ((Calendar.getInstance().timeInMillis - calendar.timeInMillis) / DateUtils.DAY_IN_MILLIS).toInt()
    }
}
