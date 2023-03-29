package net.fpoly.dailymart.security

import net.fpoly.dailymart.BuildConfig

object AESUtils {
    // mã hóa
    fun encrypt(data: String): String {
        val aesHelper = AESHelper()
        aesHelper.setKeyVector(BuildConfig.AesKey,BuildConfig.AesVector)
        var encrypt: String = ""
        try {
            encrypt = aesHelper.encrypt(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encrypt
    }

    // giải
    fun decrypt(data: String): String {
        val aesHelper = AESHelper()
        aesHelper.setKeyVector(BuildConfig.AesKey,BuildConfig.AesVector)
        return aesHelper.decrypt(data)
    }
}