package net.fpoly.dailymart.extention.num_extention

fun Int.toStrDecimal(): String {
    return if (this < 10) {
        "0" + (this).toString()
    } else {
        this.toString()
    }
}