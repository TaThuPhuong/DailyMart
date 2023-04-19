package net.fpoly.dailymart.firbase.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.fpoly.dailymart.data.model.BankInfo
import net.fpoly.dailymart.data.model.WorkSheet

object BankDao {
    private val TAG = "YinMing"
    fun insertBankInfo(bankInfo: BankInfo, message: (String, b: Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("bank").document("bank_info")
            .set(bankInfo)
            .addOnSuccessListener {
                message("Lưu thành công", true)
            }
            .addOnFailureListener {
                message("Lỗi kết nối với máy chủ", false)
                Log.e(TAG, "addOnFailureListener: $it")
            }
    }

    fun getBankInfo(getSuccess: (BankInfo?) -> Unit) {
        val db = Firebase.firestore
        db.collection("bank").document("bank_info")
            .get()
            .addOnSuccessListener { result ->
                val bankInfo = result.toObject(BankInfo::class.java)
                getSuccess(bankInfo)
            }

            .addOnFailureListener { exception ->

            }
    }
}