package net.fpoly.dailymart.extension.view_extention

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("set_src")
fun ImageView.setImage(src: Int) {
    setImageResource(src)
}

@BindingAdapter("set_input_type")
fun EditText.setType(b: Boolean) {
    transformationMethod =
        if (b) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
}

@BindingAdapter("set_icon_start")
fun TextView.setIconStart(icon: Int) {
    setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
}

@BindingAdapter("set_anim")
fun LottieAnimationView.setAnimRes(res: Int) {
    setAnimation(res)
}

@BindingAdapter("set_visible")
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("set_hide")
fun View.hideView(hide: Boolean) {
    visibility = if (hide) View.INVISIBLE else View.VISIBLE
}