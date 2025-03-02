package ir.amirreza.composepreferences.savers

import android.content.SharedPreferences
import ir.amirreza.composepreferences.models.User
import ir.amirreza.composepreferences.saver.PreferenceSaver

object UserPreferenceSaver : PreferenceSaver<User> {
    private const val KEY: String = "user"

    override fun save(value: User, sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            putString("$KEY-username", value.username)
            putString("$KEY-password", value.password)
            apply()
        }
    }

    override fun get(sharedPreferences: SharedPreferences): User {
        val username = sharedPreferences.getString("$KEY-username", "") ?: ""
        val password = sharedPreferences.getString("$KEY-password", "") ?: ""
        return User(username, password)
    }
}
