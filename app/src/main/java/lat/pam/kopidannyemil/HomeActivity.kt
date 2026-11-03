package lat.pam.kopidannyemil

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import lat.pam.kopidannyemil.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var tabs: List<LinearLayout>
    private lateinit var icons: List<ImageView>
    private lateinit var labels: List<TextView>

    private val hideToast = Runnable {
        val d = resources.displayMetrics.density
        binding.toastBar.animate().alpha(0f).translationY(12 * d)
            .setDuration(200)
            .withEndAction { binding.toastBar.visibility = View.GONE }
            .start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edgeToEdge(R.color.paper, darkStatusIcons = true, darkNavIcons = true)
        @Suppress("DEPRECATION")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val d = resources.displayMetrics.density

        tabs = listOf(binding.tabHome, binding.tabOrder, binding.tabProfile)
        icons = listOf(binding.icHome, binding.icOrder, binding.icProfile)
        labels = listOf(binding.lbHome, binding.lbOrder, binding.lbProfile)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val navArea = (84 * d).toInt() + bars.bottom
            binding.pagerHost.setPadding(0, bars.top, 0, maxOf(navArea, ime.bottom))
            (binding.navBar.layoutParams as FrameLayout.LayoutParams).bottomMargin =
                (12 * d).toInt() + bars.bottom
            (binding.toastBar.layoutParams as FrameLayout.LayoutParams).bottomMargin =
                (84 * d).toInt() + bars.bottom
            binding.navBar.requestLayout()
            binding.toastBar.requestLayout()
            insets
        }

        binding.pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> HomeFragment()
                1 -> OrderFragment()
                else -> ProfileFragment()
            }
        }
        binding.pager.offscreenPageLimit = 2
        (binding.pager.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                TabRefresh.refresh(position)
                TabRefresh.notifyShown(position)
                selectTab(position, true)
            }
        })

        binding.tabHome.setOnClickListener { goToTab(0) }
        binding.tabOrder.setOnClickListener { goToTab(1) }
        binding.tabProfile.setOnClickListener { goToTab(2) }
        binding.toastBar.setOnClickListener {
            binding.toastBar.removeCallbacks(hideToast)
            binding.toastBar.visibility = View.GONE
            goToTab(1)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.pager.currentItem != 0) {
                    binding.pager.currentItem = 0
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        selectTab(0, false)
        updateBadge()
    }

    override fun onResume() {
        super.onResume()
        updateBadge()
        TabRefresh.refreshAll()
    }

    fun goToTab(index: Int, smooth: Boolean = true) {
        binding.pager.setCurrentItem(index, smooth)
    }

    fun updateBadge() {
        val n = CartStore.totalQty()
        binding.badgeOrder.visibility = if (n > 0) View.VISIBLE else View.GONE
        binding.badgeOrder.text = if (n > 9) "9+" else n.toString()
    }

    fun onCartChanged(message: String) {
        updateBadge()
        showToast(message)
        binding.badgeOrder.animate().scaleX(1.4f).scaleY(1.4f).setDuration(120)
            .withEndAction {
                binding.badgeOrder.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }.start()
    }

    private fun showToast(message: String) {
        val d = resources.displayMetrics.density
        binding.toastBar.removeCallbacks(hideToast)
        binding.toastText.text = message
        binding.toastBar.apply {
            animate().cancel()
            visibility = View.VISIBLE
            alpha = 0f
            translationY = 16 * d
            animate().alpha(1f).translationY(0f).setDuration(220).start()
            postDelayed(hideToast, 2000)
        }
    }

    private fun selectTab(index: Int, animate: Boolean) {
        if (animate) {
            val t = AutoTransition()
            t.duration = 240
            TransitionManager.beginDelayedTransition(binding.navBar, t)
        }
        val on = Color.WHITE
        val off = Color.parseColor("#8A7B72")
        tabs.forEachIndexed { i, tab ->
            val active = i == index
            tab.setBackgroundResource(if (active) R.drawable.bg_tab_pill else 0)
            icons[i].setColorFilter(if (active) on else off)
            labels[i].visibility = if (active) View.VISIBLE else View.GONE
        }
        if (animate) {
            icons[index].apply {
                scaleX = 0.8f
                scaleY = 0.8f
                animate().scaleX(1f).scaleY(1f).setDuration(350)
                    .setInterpolator(OvershootInterpolator(2f)).start()
            }
        }
    }
}