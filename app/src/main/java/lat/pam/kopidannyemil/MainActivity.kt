package lat.pam.kopidannyemil

import android.animation.ObjectAnimator
import android.os.Build
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import lat.pam.kopidannyemil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val loops = mutableListOf<ObjectAnimator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val density = resources.displayMetrics.density

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = ContextCompat.getColor(this, R.color.night)
        if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = false
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.content) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.content.setPadding(0, bars.top, 0, 0)
            binding.btnStart.setPadding(0, 0, 0, bars.bottom)
            binding.btnStart.minHeight = (72 * density + bars.bottom).toInt()
            insets
        }

        val title = "Kopi dan Nyemil"
        val styled = SpannableString(title)
        styled.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.gold)),
            title.indexOf("Nyemil"), title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvTitle.text = styled

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        animateIn(density)
    }

    private fun animateIn(d: Float) {
        binding.imgLogo.apply {
            alpha = 0f
            translationY = -16 * d
            animate().alpha(1f).translationY(0f)
                .setDuration(600).setInterpolator(DecelerateInterpolator()).start()
        }

        binding.panelLeft.apply {
            alpha = 0f
            translationX = -60 * d
            animate().alpha(1f).translationX(0f)
                .setStartDelay(100).setDuration(700)
                .setInterpolator(DecelerateInterpolator(1.5f)).start()
        }
        binding.panelRight.apply {
            alpha = 0f
            translationX = 60 * d
            animate().alpha(1f).translationX(0f)
                .setStartDelay(200).setDuration(700)
                .setInterpolator(DecelerateInterpolator(1.5f)).start()
        }
        binding.panel.apply {
            alpha = 0f
            scaleX = 0.9f
            scaleY = 0.9f
            animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setStartDelay(150).setDuration(700)
                .setInterpolator(DecelerateInterpolator()).start()
        }

        binding.shadow.apply {
            alpha = 0f
            animate().alpha(1f).setStartDelay(600).setDuration(600).start()
        }

        binding.imgHero.apply {
            alpha = 0f
            scaleX = 0.6f
            scaleY = 0.6f
            animate().alpha(1f).scaleX(1f).scaleY(1f)
                .setStartDelay(300).setDuration(800)
                .setInterpolator(OvershootInterpolator(1.4f)).start()
        }

        listOf(binding.tvTitle to 650L, binding.tvTagline to 800L).forEach { (v, delay) ->
            v.alpha = 0f
            v.translationY = 18 * d
            v.animate().alpha(1f).translationY(0f)
                .setStartDelay(delay).setDuration(700)
                .setInterpolator(DecelerateInterpolator(1.6f)).start()
        }

        binding.btnStart.apply {
            translationY = 160 * d
            animate().translationY(0f)
                .setStartDelay(500).setDuration(700)
                .setInterpolator(DecelerateInterpolator(1.6f)).start()
        }

        loops += loop(binding.imgHero, "translationY", 0f, -10 * d, 2400)
        loops += loop(binding.imgHero, "rotation", -6f, -3f, 3000)
        loops += loop(binding.shadow, "scaleX", 1f, 0.8f, 2400)
        loops += loop(binding.panel, "rotation", -4f, -6f, 3600)
    }

    private fun loop(target: Any, prop: String, from: Float, to: Float, ms: Long): ObjectAnimator =
        ObjectAnimator.ofFloat(target, prop, from, to).apply {
            duration = ms
            startDelay = 1400
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

    override fun onDestroy() {
        loops.forEach { it.cancel() }
        super.onDestroy()
    }
}