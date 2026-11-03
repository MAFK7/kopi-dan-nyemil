package lat.pam.kopidannyemil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import lat.pam.kopidannyemil.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!
    private lateinit var gridAdapter: MenuGridAdapter
    private var chips: List<Pair<TextView, String?>> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentHomeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()

        b.rvFeatured.layoutManager =
            LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
        b.rvFeatured.adapter = FeaturedAdapter(
            MenuData.featured,
            onAdd = { addToCart(it) },
            onOpen = { openDetail(it) }
        )

        gridAdapter = MenuGridAdapter(
            onAdd = { addToCart(it) },
            onOpen = { openDetail(it) }
        )
        b.rvMenu.layoutManager = GridLayoutManager(ctx, 2)
        b.rvMenu.isNestedScrollingEnabled = false
        b.rvMenu.adapter = gridAdapter

        chips = listOf(
            b.chipAll to null,
            b.chipKopi to MenuData.KOPI,
            b.chipNonKopi to MenuData.NON_KOPI,
            b.chipCamilan to MenuData.CAMILAN
        )
        chips.forEach { (chip, cat) -> chip.setOnClickListener { selectCategory(cat) } }
        selectCategory(null)

        refresh()
        TabRefresh.listeners[0] = { refresh() }
    }

    private fun addToCart(food: FoodItem) {
        CartStore.add(food)
        (activity as? HomeActivity)?.onCartChanged("${food.name} ditambahkan")
    }

    private fun openDetail(food: FoodItem) {
        MenuDetailSheet.newInstance(food.id).show(childFragmentManager, "detail")
    }

    fun addFromSheet(food: FoodItem, qty: Int) {
        CartStore.add(food, qty)
        (activity as? HomeActivity)?.onCartChanged("${qty}x ${food.name} ditambahkan")
    }

    private fun selectCategory(cat: String?) {
        val binding = _b ?: return
        val ctx = requireContext()
        val onColor = ContextCompat.getColor(ctx, R.color.white)
        val offColor = ContextCompat.getColor(ctx, R.color.ink)

        chips.forEach { (chip, c) ->
            val selected = c == cat
            chip.setBackgroundResource(if (selected) R.drawable.bg_chip_on else R.drawable.bg_chip_off)
            chip.setTextColor(if (selected) onColor else offColor)
        }

        val list = MenuData.items.filter { cat == null || it.category == cat }
        binding.tvMenuTitle.text = cat ?: "Semua Menu"
        binding.tvCount.text = "${list.size} menu"
        gridAdapter.submit(list)
    }

    private fun refresh() {
        _b?.tvName?.setFittedText(UserStore.fullName, 26f, 16f)
    }

    override fun onDestroyView() {
        TabRefresh.listeners.remove(0)
        _b = null
        super.onDestroyView()
    }
}