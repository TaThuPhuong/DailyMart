package net.fpoly.dailymart.data.model

import java.io.Serializable

data class WorkSheet(
    val time: Long = 0,
    val day: Int = 0,
    val month: Int = 0,
    val year: Int = 0,
    val shift1: ArrayList<String> = ArrayList(),
    val shift2: ArrayList<String> = ArrayList(),
    val shift3: ArrayList<String> = ArrayList(),
) : Serializable {
    companion object {
        const val TABLE_NAME = "work_sheet"
    }
}