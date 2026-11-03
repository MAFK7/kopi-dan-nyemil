package lat.pam.kopidannyemil

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lat.pam.kopidannyemil.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var floatAnim: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edgeToEdge(R.color.cherry, darkStatusIcons = false, darkNavIcons = true)
        binding.top.padForSystemBars(top = true)
        binding.sheet.padForSystemBars(bottom = true)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        fadeUpIn(listOf(binding.imgHero, binding.imgLogo, binding.tvTitle, binding.tvSubtitle))
        floatAnim = floatAnimator(binding.imgHero, 8f)
    }

    override fun onDestroy() {
        floatAnim?.cancel()
        super.onDestroy()
    }
}