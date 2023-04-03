package net.fpoly.dailymart.data.model.param

data class RegisterParam(
    val name: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val status: Boolean = true,
    val role: String = "",
    val deviceId: String = "",
    val linkAvt: String = "",
)
