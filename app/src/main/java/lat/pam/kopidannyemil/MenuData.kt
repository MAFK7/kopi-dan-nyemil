package lat.pam.kopidannyemil

import java.util.Locale

data class FoodItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int,
    val category: String,
    val featured: Boolean = false
)

object MenuData {
    const val KOPI = "Kopi"
    const val NON_KOPI = "Non-Kopi"
    const val CAMILAN = "Camilan"

    val items = listOf(
        FoodItem(1, "Kopi Susu Gula Aren", "Espresso mantap dengan paduan susu segar & gula aren asli", 24000, R.drawable.menu_kopi_susu, KOPI, true),
        FoodItem(2, "Donat", "Donat empuk dengan glaze cokelat dan taburan sprinkles warna-warni", 12000, R.drawable.menu_donat, CAMILAN, true),
        FoodItem(3, "Sourdough Loaf", "Roti artisanal sourdough renyah di luar lembut di dalam", 48000, R.drawable.menu_sourdough, CAMILAN),
        FoodItem(4, "Cappuccino", "Kopi espresso klasik dengan steamed milk & foam lembut", 28000, R.drawable.menu_cappuccino, KOPI),
        FoodItem(5, "Matcha Latte", "Bubuk matcha premium Jepang dengan creamy milk segar", 30000, R.drawable.menu_matcha, NON_KOPI, true),
        FoodItem(6, "Americano", "Espresso pekat dengan air panas, bersih dan menyegarkan", 20000, R.drawable.menu_americano, KOPI),
        FoodItem(7, "Es Cokelat", "Cokelat pekat dengan susu dingin dan es batu", 26000, R.drawable.menu_es_cokelat, NON_KOPI),
        FoodItem(8, "Croissant", "Croissant renyah berlapis dengan aroma mentega", 22000, R.drawable.menu_croissant, CAMILAN)
    )

    val featured: List<FoodItem>
        get() = items.filter { it.featured }
}

fun formatRupiah(value: Int): String {
    return "Rp " + String.format(Locale("id", "ID"), "%,d", value)
}