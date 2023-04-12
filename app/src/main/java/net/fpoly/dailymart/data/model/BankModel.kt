package net.fpoly.dailymart.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BankModel {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("code")
    var code: String = ""
    @SerializedName("bin")
    var bin: String = ""
    @SerializedName("shortName")
    var shortName: String = ""
    @SerializedName("logo")
    var logo: String = ""
    @SerializedName("transferSupported")
    var transferSupported: Int = 0
    @SerializedName("lookupSupported")
    var lookupSupported: Int = 1
    @SerializedName("short_name")
    var short_name: String = ""
    @SerializedName("support")
    var support: Int = 3
    @SerializedName("isTransfer")
    var isTransfer: Int = 0
    @SerializedName("swift_code")
    var swift_code: String = ""
    override fun toString(): String {
        return "BankModel(id='$id', name='$name', code='$code', bin='$bin', shortName='$shortName', logo='$logo', transferSupported=$transferSupported, lookupSupported=$lookupSupported, short_name='$short_name', support=$support, isTransfer=$isTransfer, swift_code='$swift_code')\n"
    }
}

data class BankAccountCheck(var bin: Int, var accountNumber: String)

data class BankAccountRequest(
    var code: String = "",
    var desc: String = "",
    var data: AccountName? = null,
)
data class AccountName(var accountName: String)
