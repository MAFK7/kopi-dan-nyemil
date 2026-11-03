package lat.pam.kopidannyemil

object TabRefresh {
    val listeners = mutableMapOf<Int, () -> Unit>()
    val shown = mutableMapOf<Int, () -> Unit>()

    fun refresh(index: Int) {
        listeners[index]?.invoke()
    }

    fun refreshAll() {
        listeners.values.toList().forEach { it() }
    }

    fun notifyShown(index: Int) {
        shown[index]?.invoke()
    }
}