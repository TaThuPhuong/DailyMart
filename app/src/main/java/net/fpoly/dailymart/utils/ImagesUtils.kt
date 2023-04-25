package net.fpoly.dailymart.utils

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import java.io.IOException


object ImagesUtils {
    fun checkPermissionPickImage(context: Context, imv: ImageView, onChoseImage: () -> Unit) {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openImagesPicker(context, imv, onChoseImage)
            }

            override fun onPermissionDenied(deniedPermissions: List<String?>) {

            }
        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("Bạn cần cấp quyền để chọn ảnh/ chụp ảnh từ thiết bị.\n\nHãy cấp quyền cho ứng dụng [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .check()
    }

    fun openImagesPicker(context: Context, imv: ImageView, onChoseImage: () -> Unit) {
        TedImagePicker.with(context)
            .start { uri ->
                try {
                    val pathFile = URIPathHelper().getPath(context, uri)
                    val bitmap1 = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

                    val bitmap: Bitmap? = pathFile?.let { compressImageFromPath(bitmap1, it) }
                    Glide.with(context).load(bitmap)
                        .into(imv)
                    onChoseImage()

                } catch (e: Exception) {
                }
            }
    }

    private fun compressImageFromPath(bitmap: Bitmap, filePath: String): Bitmap? {
        var scaledBitmap: Bitmap? = null
        val exif: ExifInterface

        try {
            exif = ExifInterface(filePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            val matrix = Matrix()
            when (orientation) {
                6 -> {
                    matrix.postRotate(90f)
                }
                3 -> {
                    matrix.postRotate(180f)
                }
                8 -> {
                    matrix.postRotate(270f)
                }
            }
            scaledBitmap = Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width, bitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return scaledBitmap
    }
}