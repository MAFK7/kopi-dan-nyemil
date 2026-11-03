package lat.pam.kopidannyemil

import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import lat.pam.kopidannyemil.databinding.ActivityConfirmationBinding

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edgeToEdge(R.color.cherry, darkStatusIcons = false, darkNavIcons = true)
        binding.top.padForSystemBars(top = true)
        binding.sheet.padForSystemBars(bottom = true)

        val total = intent.getIntExtra("total", 0)
        val summary = intent.getStringExtra("summary").orEmpty()
        val payment = intent.getStringExtra("payment").orEmpty()

        binding.tvGreeting.text = "Halo ${UserStore.fullName},"
        binding.tvSummary.text = summary
        binding.tvPayment.text = payment
        binding.tvTotal.text = formatRupiah(total)

        binding.btnOke.setOnClickListener { finish() }

        val d = resources.displayMetrics.density
        binding.tvOk.apply {
            scaleX = 0f
            scaleY = 0f
            animate().scaleX(1f).scaleY(1f)
                .setStartDelay(250).setDuration(700)
                .setInterpolator(OvershootInterpolator(1.6f)).start()
        }
        fadeUpIn(listOf(binding.tvGreeting, binding.tvMessage), startDelay = 450L)
        binding.sheet.apply {
            translationY = 220 * d
            animate().translationY(0f)
                .setStartDelay(300).setDuration(700)
                .setInterpolator(DecelerateInterpolator(1.6f)).start()
        }
    }
}