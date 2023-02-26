package net.fpoly.dailymart.extention.view_extention

import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("set_src")
fun ImageView.setImage(src: Int) {
    setImageResource(src)
}

@BindingAdapter("set_input_type")
fun EditText.setType(b: Boolean) {

    transformationMethod =
        if (b) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
}