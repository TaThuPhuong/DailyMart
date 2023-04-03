package net.fpoly.dailymart.extension

fun String.blankException(): String = if (this.trim().isEmpty()) "* Không được để trống" else ""
