package net.fpoly.dailymart.data.model.param

data class LoginParam(
    val phoneNumber: String = "",
    val password: String = "",
) {
    fun checkValidate(): Boolean =
        !(phoneNumber.trim().isEmpty() || password.trim().isEmpty())

    companion object {
        const val SUCCESS = "Đăng nhập thành công"
        const val FAIL = "Đăng nhập thất bại"
    }
}