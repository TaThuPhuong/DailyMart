package net.fpoly.dailymart.security

class AESHelper {
    private var _aes: AES? = null
    private var key: String? = null
    private var iv: String? = null

    init {
        try {
            _aes = AES()
        } catch (e: Exception) {
        }
    }

    /**
     * Mã hóa
     *
     * @param text
     * @return
     */
    fun encrypt(text: String?): String {
        var output = ""
        try {
            output = _aes?.encrypt(text, key, iv) ?: "" //encrypt
        } catch (e: Exception) {
        }
        return output
    }

    /**
     * Giải mã
     *
     * @param text
     * @return
     */
    fun decrypt(text: String?): String {
        var output = ""
        try {
            output = _aes?.decrypt(text, key, iv) ?: "" //decrypt
        } catch (e: Exception) {
        }
        return output
    }

    fun setKeyVector(key: String?, iv: String?) {
        this.key = key
        this.iv = iv
    }
}