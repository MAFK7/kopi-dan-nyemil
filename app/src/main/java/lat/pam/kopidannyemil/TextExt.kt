package lat.pam.kopidannyemil

import android.util.TypedValue
import android.widget.TextView

fun TextView.setFittedText(value: String, maxSp: Float, minSp: Float) {
    text = value
    val range = maxSp - minSp
    val size = when {
        value.length <= 12 -> maxSp
        value.length <= 18 -> maxSp - range * 0.35f
        value.length <= 24 -> maxSp - range * 0.7f
        else -> minSp
    }
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}