package lat.pam.kopidannyemil

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

object UserStore {
    private const val PREF = "kopi_user"
    private const val DEFAULT_NAME = "M. Asad Farhan Khomeini"
    private const val DEFAULT_USER = "sadd"
    private const val DEFAULT_PASS = "0000"

    private var prefs: SharedPreferences? = null

    var fullName: String = DEFAULT_NAME
        set(value) {
            field = value
            prefs?.edit()?.putString("fullName", value)?.apply()
        }

    var username: String = DEFAULT_USER
        private set

    private var passwordHash: String = hash(DEFAULT_PASS)

    fun init(context: Context) {
        val p = context.applicationContext.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        prefs = p
        p.getString("fullName", null)?.let { fullName = it }
        username = p.getString("username", DEFAULT_USER) ?: DEFAULT_USER
        passwordHash = p.getString("passwordHash", null) ?: hash(DEFAULT_PASS)
    }

    fun register(name: String, user: String, pass: String) {
        fullName = name
        username = user
        passwordHash = hash(pass)
        prefs?.edit()
            ?.putString("fullName", name)
            ?.putString("username", user)
            ?.putString("passwordHash", passwordHash)
            ?.apply()
    }

    fun checkLogin(user: String, pass: String): Boolean =
        user.equals(username, ignoreCase = true) && hash(pass) == passwordHash

    private fun hash(text: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(text.toByteArray())
            .joinToString("") { "%02x".format(it) }
}