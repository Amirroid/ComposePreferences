package ir.amirreza.composepreferences.state

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
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
    private val key: String?,
    private val defaultValue: T,
    private val saver: PreferenceSaver<T>? = null
) : MutableState<T> {

    init {
        require(!(key == null && saver == null)) { "At least one of 'key' or 'saver' must be non-null." }
    }

    private val _state = mutableStateOf(loadValue())

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (saver == null) {
                if (changedKey == key) {
                    _state.value = loadValue()
                }
            } else {
                _state.value = loadValue()
            }
        }

    override var value: T
        get() = _state.value
        set(newValue) {
            _state.value = newValue
            storeValue(newValue)
        }

    override fun component1(): T = value

    override fun component2(): (T) -> Unit = { newValue: T -> value = newValue }

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

    fun registerListener() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    fun disposeListener() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}

/**
 * Creates a preference state that syncs with SharedPreferences.
 */
fun <T : Any> preferenceStateOf(
    key: String?,
    defaultValue: T,
    sharedPreferences: SharedPreferences,
    saver: PreferenceSaver<T>?,
) = PreferenceMutableState(
    key = key,
    defaultValue = defaultValue,
    sharedPreferences = sharedPreferences,
    saver = saver
)

/**
 * Remembers a preference state that syncs with SharedPreferences using a key.
 */
@Composable
fun <T : Any> rememberPreferenceStateOf(
    key: String,
    defaultValue: T,
    sharedPreferences: SharedPreferences = LocalSharedPreferences.current ?: defaultPreferences(),
    vararg keys: Any
): MutableState<T> {
    return remember(*keys) {
        preferenceStateOf(key, defaultValue, sharedPreferences, null)
    }.also { state ->
        DisposableEffect(*keys) {
            state.registerListener()
            onDispose {
                state.disposeListener()
            }
        }
    }
}

/**
 * Remembers a preference state that syncs with SharedPreferences using a custom saver.
 */
@Composable
fun <T : Any> rememberPreferenceStateOf(
    saver: PreferenceSaver<T>? = null,
    defaultValue: T,
    sharedPreferences: SharedPreferences = LocalSharedPreferences.current ?: defaultPreferences(),
    vararg keys: Any
): MutableState<T> {
    return remember(*keys) {
        preferenceStateOf(null, defaultValue, sharedPreferences, saver)
    }.also { state ->
        DisposableEffect(*keys) {
            state.registerListener()
            onDispose {
                state.disposeListener()
            }
        }
    }
}