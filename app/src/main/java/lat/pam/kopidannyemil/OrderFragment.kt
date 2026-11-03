package lat.pam.kopidannyemil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import lat.pam.kopidannyemil.databinding.FragmentOrderBinding
import lat.pam.kopidannyemil.databinding.ItemCartBinding
import lat.pam.kopidannyemil.databinding.ItemPayOptionBinding
import lat.pam.kopidannyemil.databinding.ItemSuggestBinding

class OrderFragment : Fragment() {
    private var _b: FragmentOrderBinding? = null
    private val b get() = _b!!

    private var selected = Payment.COD
    private val payViews = mutableMapOf<Payment, ItemPayOptionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentOrderBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.btnBrowse.setOnClickListener { (activity as? HomeActivity)?.goToTab(0) }
        b.btnOrder.setOnClickListener { placeOrder() }

        buildSuggestions()
        buildPayOptions()
        render()
        TabRefresh.listeners[1] = { render() }
        TabRefresh.shown[1] = { playEmptyEntrance() }
    }

    private fun buildSuggestions() {
        b.suggestContainer.removeAllViews()
        MenuData.featured.take(2).forEach { food ->
            val row = ItemSuggestBinding.inflate(layoutInflater, b.suggestContainer, false)
            row.imgItem.setImageResource(food.imageRes)
            row.tvName.text = food.name
            row.tvPrice.text = formatRupiah(food.price)
            row.btnAdd.setOnClickListener {
                CartStore.add(food)
                render()
            }
            b.suggestContainer.addView(row.root)
        }
    }

    private fun playEmptyEntrance() {
        val binding = _b ?: return
        if (CartStore.items.isNotEmpty()) return
        fadeUpIn(
            listOf(binding.emptyCard, binding.tvFav, binding.suggestContainer),
            startDelay = 40L,
            step = 100L
        )
        binding.imgEmptyHero.apply {
            scaleX = 0.7f
            scaleY = 0.7f
            animate().scaleX(1f).scaleY(1f)
                .setStartDelay(220).setDuration(700)
                .setInterpolator(OvershootInterpolator(1.8f)).start()
        }
    }

    private fun buildPayOptions() {
        b.payContainer.removeAllViews()
        payViews.clear()
        Payment.values().forEach { p ->
            val row = ItemPayOptionBinding.inflate(layoutInflater, b.payContainer, false)
            row.tvTitle.text = p.title
            row.tvSub.text = p.subtitle
            row.root.setOnClickListener {
                selected = p
                updatePay()
            }
            b.payContainer.addView(row.root)
            payViews[p] = row
        }
        updatePay()
    }

    private fun updatePay() {
        payViews.forEach { (p, row) ->
            val on = p == selected
            row.root.setBackgroundResource(if (on) R.drawable.bg_pay_on else R.drawable.bg_pay_off)
            row.radio.setBackgroundResource(if (on) R.drawable.bg_radio_on else R.drawable.bg_radio_off)
        }
    }

    private fun render() {
        val binding = _b ?: return
        val empty = CartStore.items.isEmpty()
        binding.emptyState.visibility = if (empty) View.VISIBLE else View.GONE
        binding.filledState.visibility = if (empty) View.GONE else View.VISIBLE
        binding.ctaBar.visibility = if (empty) View.GONE else View.VISIBLE

        if (binding.etName.text.isNullOrBlank()) binding.etName.setText(UserStore.fullName)

        binding.cartContainer.removeAllViews()
        for (item in CartStore.items.toList()) {
            val row = ItemCartBinding.inflate(layoutInflater, binding.cartContainer, false)
            row.imgItem.setImageResource(item.food.imageRes)
            row.tvName.text = item.food.name
            row.tvSubtotal.text = formatRupiah(item.food.price * item.qty)
            row.tvQty.text = item.qty.toString()

            row.btnPlus.setOnClickListener {
                item.qty++
                render()
            }
            row.btnMinus.setOnClickListener {
                item.qty--
                if (item.qty <= 0) CartStore.items.remove(item)
                render()
            }
            binding.cartContainer.addView(row.root)
        }

        val total = CartStore.totalPrice()
        binding.tvTotal.text = formatRupiah(total)
        binding.btnOrder.text = "Order dan Kirim  ·  ${formatRupiah(total)}"
        (activity as? HomeActivity)?.updateBadge()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(b.root.windowToken, 0)
        b.root.clearFocus()
    }

    private fun placeOrder() {
        val name = b.etName.text.toString().trim()
        val address = b.etAddress.text.toString().trim()

        if (CartStore.items.isEmpty()) {
            Toast.makeText(requireContext(), "Keranjang masih kosong", Toast.LENGTH_SHORT).show()
            return
        }
        if (name.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "Nama lengkap dan alamat wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        hideKeyboard()

        if (selected == Payment.COD) {
            completeOrder()
        } else {
            PaymentSheet.newInstance(selected, CartStore.totalPrice())
                .show(childFragmentManager, "pay")
        }
    }

    fun completeOrder() {
        val binding = _b ?: return
        if (CartStore.items.isEmpty()) return

        val total = CartStore.totalPrice()
        val summary = CartStore.items.joinToString("\n") { "${it.qty}x ${it.food.name}" }
        val paymentText =
            if (selected == Payment.COD) selected.title else "${selected.title} · Lunas"

        CartStore.clear()
        binding.etAddress.text?.clear()
        binding.etNote.text?.clear()
        selected = Payment.COD
        updatePay()
        render()
        (activity as? HomeActivity)?.goToTab(0, false)

        val next = Intent(requireContext(), ConfirmationActivity::class.java)
        next.putExtra("summary", summary)
        next.putExtra("total", total)
        next.putExtra("payment", paymentText)
        startActivity(next)
    }

    override fun onDestroyView() {
        TabRefresh.listeners.remove(1)
        TabRefresh.shown.remove(1)
        payViews.clear()
        _b = null
        super.onDestroyView()
    }
}