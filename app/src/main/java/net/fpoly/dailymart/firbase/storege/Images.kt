package net.fpoly.dailymart.firbase.storege

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

object Images {

    private val storage = Firebase.storage

    fun uploadImage(
        imv: ImageView,
        tableName: String,
        id: String,
        callback: ((String) -> Unit)? = null,
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

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {

        }
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                callback?.invoke(downloadUri)
            }
        }
    }
}
