package net.fpoly.dailymart.firbase.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import net.fpoly.dailymart.data.model.ReportCheckStock
import net.fpoly.dailymart.data.model.dataToString
import net.fpoly.dailymart.data.model.stringToData

object ReportStockDao {

    fun insertReport(report: ReportCheckStock, onSuccess: (Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection("ReportStock").document(report.time.toString())
            .set(report)
            .addOnSuccessListener {
                onSuccess(true)
            }
            .addOnFailureListener {
                onSuccess(false)
            }
    }

    fun getReport(getSuccess: (List<ReportCheckStock>) -> Unit) {
        val db = Firebase.firestore
        val list = ArrayList<ReportCheckStock>()
        db.collection("ReportStock")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val res = document.toObject(ReportCheckStock::class.java)
                    Log.e("YingMing", "getReport: $res")
                    list.add(res)
                }
                getSuccess(list.sortedByDescending { it.time })
            }

            .addOnFailureListener { exception ->

            }
    }
}