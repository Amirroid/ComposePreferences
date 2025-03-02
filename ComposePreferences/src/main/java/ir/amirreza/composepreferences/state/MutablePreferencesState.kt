package ir.amirreza.composepreferences.state

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ir.amirreza.composepreferences.compositions.LocalSharedPreferences
import ir.amirreza.composepreferences.utils.defaultPreferences


/**
 * A custom MutableState that syncs with SharedPreferences.
 */
class PreferenceMutableState<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: T
) : MutableState<T> {

    private val _state = mutableStateOf(getStoredValue())

    override var value: T
        get() = _state.value
        set(newValue) {
            _state.value = newValue
            saveValue(newValue)
        }

    override fun component1() = value
    override fun component2() = { newValue: T -> value = newValue }

    @Suppress("UNCHECKED_CAST")
    private fun getStoredValue(): T {
        return when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) ?: defaultValue
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            is Set<*> -> sharedPreferences.getStringSet(key, defaultValue as Set<String>)
                ?: defaultValue

            is Double -> Double.fromBits(sharedPreferences.getLong(key, defaultValue.toBits()))
            else -> defaultValue
        } as T
    }

    private fun saveValue(value: T) {
        with(sharedPreferences.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Double -> putLong(key, value.toBits())
                else -> throw IllegalArgumentException("Unsupported type for SharedPreferences")
            }
            apply()
        }
    }
}

@Composable
fun <T : Any> rememberPreferenceStateOf(
    key: String,
    defaultValue: T,
    sharedPreferences: SharedPreferences = LocalSharedPreferences.current ?: defaultPreferences(),
    vararg keys: Any
): MutableState<T> {
    return remember(*keys) {
        PreferenceMutableState(
            sharedPreferences = sharedPreferences,
            key = key,
            defaultValue = defaultValue
        )
    }
}