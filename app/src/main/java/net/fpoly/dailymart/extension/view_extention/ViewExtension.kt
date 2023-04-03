package net.fpoly.dailymart.extension.view_extention

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.isShowing(): Boolean {
    return visibility == View.VISIBLE
}

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun EditText.getTextOnChange(onTextChange: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChange(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }

    })
}

@SuppressLint("InternalInsetResource", "DiscouragedApi")
private fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun View.setMarginsStatusBar(context: Context) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(0, getStatusBarHeight(context), 0, 0)
        requestLayout()
    }
}
