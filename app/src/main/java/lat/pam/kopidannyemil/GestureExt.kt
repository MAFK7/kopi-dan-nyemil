package lat.pam.kopidannyemil

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class HorizontalGestureGuard {
    private var startX = 0f
    private var startY = 0f

    fun onEvent(view: View, e: MotionEvent) {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.x
                startY = e.y
                view.parent?.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = e.x - startX
                val dy = e.y - startY
                if (abs(dx) > abs(dy)) {
                    val direction = if (dx > 0) -1 else 1
                    view.parent?.requestDisallowInterceptTouchEvent(view.canScrollHorizontally(direction))
                } else {
                    view.parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                view.parent?.requestDisallowInterceptTouchEvent(false)
        }
    }
}

class HorizontalRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private val guard = HorizontalGestureGuard()

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        guard.onEvent(this, e)
        return super.dispatchTouchEvent(e)
    }
}

class HorizontalScroller @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {
    private val guard = HorizontalGestureGuard()

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        guard.onEvent(this, e)
        return super.dispatchTouchEvent(e)
    }
}