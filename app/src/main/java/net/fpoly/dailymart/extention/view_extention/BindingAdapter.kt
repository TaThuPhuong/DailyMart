package net.fpoly.dailymart.extention.view_extention

import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("set_src")
fun ImageView.setImage(src: Int) {
    setImageResource(src)
}

@BindingAdapter("set_input_type")
fun EditText.setInputType(b: Boolean) {
    inputType =
        if (b) InputType.TYPE_TEXT_VARIATION_NORMAL else InputType.TYPE_TEXT_VARIATION_PASSWORD
}