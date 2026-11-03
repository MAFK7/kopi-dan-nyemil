package lat.pam.kopidannyemil

enum class Payment(val title: String, val subtitle: String) {
    COD("Bayar di Tempat (COD)", "Bayar tunai saat pesanan sampai"),
    QRIS("QRIS", "Scan kode QR dengan e-wallet atau mobile banking"),
    TRANSFER("Transfer Bank", "Transfer ke rekening toko")
}