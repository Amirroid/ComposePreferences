package ir.amirreza.composepreferences.state

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ir.amirreza.composepreferences.compositions.LocalSharedPreferences
import ir.amirreza.composepreferences.saver.PreferenceSaver
import ir.amirreza.composepreferences.utils.defaultPreferences


/**
 * A custom MutableState that syncs with SharedPreferences.
 */
@Stable
class PreferenceMutableState<T>(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: T,
    private val saver: PreferenceSaver<T>? = null
) : MutableState<T> {

    private val _state = mutableStateOf(loadValue())

    override var value: T
        get() = _state.value
        set(newValue) {
            _state.value = newValue
            storeValue(newValue)
        }

    override fun component1() = value
    override fun component2() = { newValue: T -> value = newValue }

    @Suppress("UNCHECKED_CAST")
    private fun loadValue(): T = saver?.get(sharedPreferences) ?: run {
        when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) ?: defaultValue
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            is Set<*> -> sharedPreferences.getStringSet(key, defaultValue.filterString())
                ?: defaultValue

            is Double -> Double.fromBits(sharedPreferences.getLong(key, defaultValue.toBits()))
            else -> defaultValue
        }
    } as T

    private fun storeValue(value: T) {
        saver?.save(value, sharedPreferences) ?: with(sharedPreferences.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is Double -> putLong(key, value.toBits())
                is Set<*> -> putStringSet(key, value.filterString())
                else -> throw IllegalArgumentException("Unsupported type for SharedPreferences")
            }
            apply()
        }
    }

    private fun Set<*>.filterString() = filterIsInstance<String>().toSet()
}

fun <T : Any> preferenceStateOf(
    key: String,
    defaultValue: T,
    sharedPreferences: SharedPreferences,
    saver: PreferenceSaver<T>? = null,
) = PreferenceMutableState(
    key = key,
    defaultValue = defaultValue,
    sharedPreferences = sharedPreferences,
    saver = saver
)

@Composable
fun <T : Any> rememberPreferenceStateOf(
    key: String,
    defaultValue: T,
    sharedPreferences: SharedPreferences = LocalSharedPreferences.current ?: defaultPreferences(),
    saver: PreferenceSaver<T>? = null,
    vararg keys: Any
): MutableState<T> = remember(*keys) {
    preferenceStateOf(key, defaultValue, sharedPreferences, saver)
}