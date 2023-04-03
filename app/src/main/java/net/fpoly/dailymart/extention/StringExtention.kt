package net.fpoly.dailymart.extention

fun String.blankException(): String = if (this.trim().isEmpty()) "* Không được để trống" else ""
