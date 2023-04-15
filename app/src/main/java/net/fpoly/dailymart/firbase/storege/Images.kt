package net.fpoly.dailymart.firbase.storege

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

object Images {

    private val storage = Firebase.storage
    private val TAG = "YingMing"
    fun uploadImage(
        imv: ImageView,
        tableName: String,
        id: String,
        onSuccess: ((String) -> Unit)? = null,
        onFail: () -> Unit,
    ) {
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$tableName/$id")
        imv.isDrawingCacheEnabled = true
        imv.buildDrawingCache()
        val bitmap = (imv.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
                onFail()
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                onSuccess?.invoke(downloadUri)
            }
        }
    }
}
