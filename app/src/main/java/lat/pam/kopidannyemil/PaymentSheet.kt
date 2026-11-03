package lat.pam.kopidannyemil

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lat.pam.kopidannyemil.databinding.SheetPaymentBinding
import java.util.Locale

class PaymentSheet : BottomSheetDialogFragment() {
    private var _b: SheetPaymentBinding? = null
    private val b get() = _b!!

    private val handler = Handler(Looper.getMainLooper())
    private var timer: CountDownTimer? = null
    private var confirmed = false

    companion object {
        private const val CONFIRM_DELAY_MS = 6000L
        private const val EXPIRE_MS = 300_000L

        fun newInstance(method: Payment, total: Int) = PaymentSheet().apply {
            arguments = Bundle().apply {
                putString("method", method.name)
                putInt("total", total)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = SheetPaymentBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val method = Payment.valueOf(requireArguments().getString("method") ?: Payment.QRIS.name)
        val total = requireArguments().getInt("total")
        val isQris = method == Payment.QRIS

        b.tvTitle.text = if (isQris) "Bayar dengan QRIS" else "Transfer Bank"
        b.tvSubtitle.text =
            if (isQris) "Scan kode QR ini dengan e-wallet atau mobile banking."
            else "Transfer sesuai total ke rekening di bawah ini."
        b.qrBox.visibility = if (isQris) View.VISIBLE else View.GONE
        b.bankBox.visibility = if (isQris) View.GONE else View.VISIBLE
        b.tvTotal.text = formatRupiah(total)

        b.btnCancel.setOnClickListener { dismiss() }

        startTimer()
        handler.postDelayed({ confirmPayment() }, CONFIRM_DELAY_MS)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(EXPIRE_MS, 1000) {
            override fun onTick(ms: Long) {
                val s = (ms / 1000).toInt()
                _b?.tvTimer?.text =
                    String.format(Locale.US, "Selesaikan dalam %02d:%02d", s / 60, s % 60)
            }

            override fun onFinish() {
                if (confirmed || _b == null) return
                context?.let {
                    Toast.makeText(it, "Waktu pembayaran habis", Toast.LENGTH_SHORT).show()
                }
                dismissAllowingStateLoss()
            }
        }.start()
    }

    private fun confirmPayment() {
        if (confirmed || _b == null) return
        confirmed = true
        timer?.cancel()
        isCancelable = false
        val host = parentFragment as? OrderFragment

        b.progress.visibility = View.GONE
        b.tvCheck.visibility = View.VISIBLE
        b.tvCheck.scaleX = 0f
        b.tvCheck.scaleY = 0f
        b.tvCheck.animate().scaleX(1f).scaleY(1f).setDuration(450)
            .setInterpolator(OvershootInterpolator(2.2f)).start()
        b.tvStatus.text = "Pembayaran diterima"
        b.tvTimer.text = "Pesananmu segera diproses"
        b.btnCancel.visibility = View.GONE

        handler.postDelayed({
            dismissAllowingStateLoss()
            host?.completeOrder()
        }, 1300)
    }

    override fun onStart() {
        super.onStart()
        styleSheet()
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        timer?.cancel()
        _b = null
        super.onDestroyView()
    }
}