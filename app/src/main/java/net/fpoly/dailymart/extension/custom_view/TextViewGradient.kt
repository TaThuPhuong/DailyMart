package net.fpoly.dailymart.extension.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import net.fpoly.dailymart.R

class TextViewGradient : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            paint.shader = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                ContextCompat.getColor(context, R.color.start_text),
                ContextCompat.getColor(context, R.color.end_text),
                Shader.TileMode.CLAMP
            )
            paint.textSize = textSize
        }
    }
}