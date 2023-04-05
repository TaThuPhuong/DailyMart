package net.fpoly.dailymart.data.model.response

data class ResponseResult<T>(
    var status: Int = 0,
    var message: String?,
    var data: T?
) {
    fun isSuccess(): Boolean {
        return status == 1
    }
}
