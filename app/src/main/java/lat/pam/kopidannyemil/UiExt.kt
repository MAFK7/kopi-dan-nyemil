package lat.pam.kopidannyemil

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

fun AppCompatActivity.edgeToEdge(windowBgRes: Int, darkStatusIcons: Boolean, darkNavIcons: Boolean) {
    window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, windowBgRes)))
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = false
    WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = darkStatusIcons
        isAppearanceLightNavigationBars = darkNavIcons
    }
}

fun View.padForSystemBars(top: Boolean = false, bottom: Boolean = false) {
    val l = paddingLeft
    val t = paddingTop
    val r = paddingRight
    val b = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        v.setPadding(
            l,
            if (top) t + bars.top else t,
            r,
            if (bottom) b + maxOf(bars.bottom, ime.bottom) else b
        )
        insets
    }
    ViewCompat.requestApplyInsets(this)
}

fun fadeUpIn(views: List<View>, startDelay: Long = 250L, step: Long = 90L) {
    views.forEachIndexed { i, v ->
        val d = v.resources.displayMetrics.density
        v.alpha = 0f
        v.translationY = 16 * d
        v.animate().alpha(1f).translationY(0f)
            .setStartDelay(startDelay + step * i)
            .setDuration(600)
            .setInterpolator(DecelerateInterpolator(1.6f))
            .start()
    }
}

fun floatAnimator(v: View, distanceDp: Float, ms: Long = 2800L, delay: Long = 1200L): ObjectAnimator {
    val d = v.resources.displayMetrics.density
    return ObjectAnimator.ofFloat(v, "translationY", 0f, -distanceDp * d).apply {
        duration = ms
        startDelay = delay
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}