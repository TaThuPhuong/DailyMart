package net.fpoly.dailymart.data.model.param

data class ChangePassParam(
    val id: String = "",
    val phoneNumber: String = "",
    val oldPass: String = "",
    val newPass: String = "",
)
