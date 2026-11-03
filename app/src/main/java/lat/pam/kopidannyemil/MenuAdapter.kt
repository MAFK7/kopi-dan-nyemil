package lat.pam.kopidannyemil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.RecyclerView
import lat.pam.kopidannyemil.databinding.ItemFeaturedBinding
import lat.pam.kopidannyemil.databinding.ItemMenuGridBinding

private fun View.bump() {
    animate().cancel()
    animate().scaleX(0.8f).scaleY(0.8f).setDuration(80).withEndAction {
        animate().scaleX(1f).scaleY(1f).setDuration(240)
            .setInterpolator(OvershootInterpolator(3f)).start()
    }.start()
}

class MenuGridAdapter(
    private val onAdd: (FoodItem) -> Unit,
    private val onOpen: (FoodItem) -> Unit
) : RecyclerView.Adapter<MenuGridAdapter.VH>() {

    private var items: List<FoodItem> = emptyList()

    class VH(val b: ItemMenuGridBinding) : RecyclerView.ViewHolder(b.root)

    fun submit(list: List<FoodItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemMenuGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val food = items[position]
        holder.b.imgMenu.setImageResource(food.imageRes)
        holder.b.tvName.text = food.name
        holder.b.tvCategory.text = food.category
        holder.b.tvPrice.text = formatRupiah(food.price)
        holder.b.btnAdd.setOnClickListener {
            it.bump()
            onAdd(food)
        }
        holder.itemView.setOnClickListener { onOpen(food) }

        val v = holder.itemView
        v.alpha = 0f
        v.translationY = 24 * v.resources.displayMetrics.density
        v.animate().alpha(1f).translationY(0f)
            .setStartDelay(position * 45L).setDuration(320)
            .setInterpolator(DecelerateInterpolator(1.5f)).start()
    }
}

class FeaturedAdapter(
    private val items: List<FoodItem>,
    private val onAdd: (FoodItem) -> Unit,
    private val onOpen: (FoodItem) -> Unit
) : RecyclerView.Adapter<FeaturedAdapter.VH>() {

    class VH(val b: ItemFeaturedBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemFeaturedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val food = items[position]
        holder.b.imgFeatured.setImageResource(food.imageRes)
        holder.b.tvName.text = food.name
        holder.b.tvPrice.text = formatRupiah(food.price)
        holder.b.btnAdd.setOnClickListener {
            it.bump()
            onAdd(food)
        }
        holder.itemView.setOnClickListener { onOpen(food) }
    }
}