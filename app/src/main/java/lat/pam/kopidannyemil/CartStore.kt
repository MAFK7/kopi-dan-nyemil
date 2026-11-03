package lat.pam.kopidannyemil

data class CartItem(val food: FoodItem, var qty: Int)

object CartStore {
    val items = mutableListOf<CartItem>()

    fun add(food: FoodItem, qty: Int = 1) {
        val existing = items.find { it.food.id == food.id }
        if (existing != null) existing.qty += qty else items.add(CartItem(food, qty))
    }

    fun totalPrice(): Int = items.sumOf { it.food.price * it.qty }

    fun totalQty(): Int = items.sumOf { it.qty }

    fun clear() = items.clear()
}