package net.fpoly.dailymart.firbase.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.fpoly.dailymart.data.model.WorkSheet

object WorkSheetDao {

    private val TAG = "YingMing"
    fun insertWorkSheet(sheet: WorkSheet, day: String, onSuccess: (Boolean) -> Unit) {
        val db = Firebase.firestore
        db.collection(WorkSheet.TABLE_NAME).document(day)
            .set(sheet)
            .addOnSuccessListener {
                onSuccess(true)
            }
            .addOnFailureListener {
                onSuccess(false)
                Log.e(TAG, "addOnFailureListener: $it")
            }
    }

    fun getWorkSheet(month: Int, year: Int, getSuccess: (ArrayList<WorkSheet>) -> Unit) {
        val db = Firebase.firestore
        val list = ArrayList<WorkSheet>()
        Log.e(TAG, "day: $month - year :$year")
        db.collection(WorkSheet.TABLE_NAME)
            .whereEqualTo("month", month)
            .whereEqualTo("year", year)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val sheet = document.toObject(WorkSheet::class.java)
                    list.add(sheet)
                }
                getSuccess(list)
            }

            .addOnFailureListener { exception ->

            }
    }
}