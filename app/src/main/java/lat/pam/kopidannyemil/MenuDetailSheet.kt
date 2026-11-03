package lat.pam.kopidannyemil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import lat.pam.kopidannyemil.databinding.SheetMenuDetailBinding

class MenuDetailSheet : BottomSheetDialogFragment() {
    private var _b: SheetMenuDetailBinding? = null
    private val b get() = _b!!
    private var qty = 1
    private lateinit var food: FoodItem

    companion object {
        fun newInstance(id: Int) = MenuDetailSheet().apply {
            arguments = Bundle().apply { putInt("id", id) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = SheetMenuDetailBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt("id")
        food = MenuData.items.first { it.id == id }

        b.imgFood.setImageResource(food.imageRes)
        b.tvCategory.text = food.category
        b.tvName.text = food.name
        b.tvPrice.text = formatRupiah(food.price)
        b.tvDesc.text = food.description
        updateQty()

        b.btnMinus.setOnClickListener {
            if (qty > 1) {
                qty--
                updateQty()
            }
        }
        b.btnPlus.setOnClickListener {
            if (qty < 20) {
                qty++
                updateQty()
            }
        }
        b.btnAdd.setOnClickListener {
            val host = parentFragment as? HomeFragment
            val chosenQty = qty
            dismissAllowingStateLoss()
            host?.addFromSheet(food, chosenQty)
        }
    }

    private fun updateQty() {
        b.tvQty.text = qty.toString()
        b.btnAdd.text = "Tambah ke pesanan  ·  ${formatRupiah(food.price * qty)}"
    }

    override fun onStart() {
        super.onStart()
        styleSheet()
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}