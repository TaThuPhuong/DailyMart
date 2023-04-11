package net.fpoly.dailymart.data.model.root

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.fpoly.dailymart.data.model.BankModel

class BankRoot {
    @SerializedName("code")
    @Expose
    var code: String = ""

    @SerializedName("desc")
    @Expose
    var desc: String = ""

    @SerializedName("data")
    @Expose
    var data: ArrayList<BankModel> = ArrayList()
    override fun toString(): String {
        return "BankRoot(code='$code', desc='$desc', data='$data')"
    }
}