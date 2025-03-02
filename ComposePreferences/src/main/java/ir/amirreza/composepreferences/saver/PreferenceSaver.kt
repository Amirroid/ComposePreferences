package ir.amirreza.composepreferences.saver

import android.content.SharedPreferences

/**
 * An interface for defining custom serialization and deserialization of values
 * stored in SharedPreferences. This allows handling complex types beyond primitives.
 */
interface PreferenceSaver<T> {

    /**
     * Saves the given value into SharedPreferences.
     *
     * @param value The value to be stored.
     * @param sharedPreferences The SharedPreferences instance where the value should be saved.
     */
    fun save(value: T, sharedPreferences: SharedPreferences)

    /**
     * Retrieves the stored value from SharedPreferences.
     *
     * @param sharedPreferences The SharedPreferences instance from which the value should be retrieved.
     * @return The stored value of type T.
     */
    fun get(sharedPreferences: SharedPreferences): T
}