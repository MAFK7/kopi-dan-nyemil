package lat.pam.kopidannyemil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import lat.pam.kopidannyemil.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _b: FragmentProfileBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentProfileBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.btnSave.setOnClickListener {
            val name = b.etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            UserStore.fullName = name
            refresh()
            TabRefresh.refresh(0)
            Toast.makeText(requireContext(), "Profil disimpan", Toast.LENGTH_SHORT).show()
        }

        b.btnLogout.setOnClickListener {
            CartStore.clear()
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        refresh()
        TabRefresh.listeners[2] = { refresh() }
        TabRefresh.shown[2] = { playEntrance() }
    }

    private fun refresh() {
        val binding = _b ?: return
        val name = UserStore.fullName.trim()
        binding.tvAvatar.text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
        binding.tvName.setFittedText(name, 22f, 14f)
        binding.tvUsername.text = "@${UserStore.username}"
        if (!binding.etName.hasFocus()) binding.etName.setText(name)
    }

    private fun playEntrance() {
        val binding = _b ?: return
        fadeUpIn(
            listOf(binding.cardMember, binding.tvSection, binding.cardSettings, binding.btnLogout),
            startDelay = 40L,
            step = 90L
        )
        binding.tvAvatar.apply {
            scaleX = 0.6f
            scaleY = 0.6f
            animate().scaleX(1f).scaleY(1f)
                .setStartDelay(250).setDuration(600)
                .setInterpolator(OvershootInterpolator(2.5f)).start()
        }
    }

    override fun onDestroyView() {
        TabRefresh.listeners.remove(2)
        TabRefresh.shown.remove(2)
        _b = null
        super.onDestroyView()
    }
}