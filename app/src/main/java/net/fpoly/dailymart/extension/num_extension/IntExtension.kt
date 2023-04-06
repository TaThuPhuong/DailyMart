package net.fpoly.dailymart.extension.num_extension

fun Int.toStrDecimal(): String {
    return if (this < 10) {
        "0" + (this).toString()
    } else {
        this.toString()
    }
}