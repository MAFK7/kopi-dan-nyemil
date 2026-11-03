package lat.pam.kopidannyemil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lat.pam.kopidannyemil.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edgeToEdge(R.color.cherry, darkStatusIcons = false, darkNavIcons = true)
        binding.header.padForSystemBars(top = true)
        binding.sheet.padForSystemBars(bottom = true)

        binding.btnBack.setOnClickListener { finish() }
        binding.tvToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            when {
                fullName.isEmpty() -> toast("Nama lengkap wajib diisi")
                username.length < 3 -> toast("Username minimal 3 karakter")
                username.contains(" ") -> toast("Username tidak boleh mengandung spasi")
                password.length < 6 -> toast("Password minimal 6 karakter")
                else -> {
                    UserStore.register(fullName, username, password)
                    toast("Registrasi berhasil")
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        fadeUpIn(listOf(binding.tvTitle, binding.tvSubtitle))
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}