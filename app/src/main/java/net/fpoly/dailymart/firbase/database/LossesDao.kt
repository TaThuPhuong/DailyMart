package net.fpoly.dailymart.firbase.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.fpoly.dailymart.data.model.Losses

object LossesDao {
    private val TAG = "YingMing"
    fun insert(losses: Losses, onSuccess: (b: Boolean) -> Unit) {
        val db = Firebase.firestore
        try {
            db.collection("Losses").document(losses.time.toString())
                .set(losses)
                .addOnSuccessListener {
                    onSuccess(true)
                }
                .addOnFailureListener {
                    onSuccess(false)
                    Log.e(TAG, "addOnFailureListener: $it")
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLossesByMonth(month: Int, year: Int, onGetSuccess: (Long) -> Unit) {
        val db = Firebase.firestore
        val list = ArrayList<Losses>()
        try {
            db.collection("Losses")
                .whereEqualTo("month", month)
                .whereEqualTo("year", year)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val losses = document.toObject(Losses::class.java)
                        list.add(losses)
                    }
                    onGetSuccess(list.sumOf { it.money })
                }

                .addOnFailureListener { exception ->
                    Log.e(TAG, "addOnFailureListener: $exception")
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLossesByYear(year: Int, onGetSuccess: (Long) -> Unit) {
        val db = Firebase.firestore
        val list = ArrayList<Losses>()
        try {
            db.collection("Losses")
                .whereEqualTo("year", year)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val losses = document.toObject(Losses::class.java)
                        list.add(losses)
                    }
                    onGetSuccess(list.sumOf { it.money })
                }

                .addOnFailureListener { exception ->
                    Log.e(TAG, "addOnFailureListener: $exception")
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}