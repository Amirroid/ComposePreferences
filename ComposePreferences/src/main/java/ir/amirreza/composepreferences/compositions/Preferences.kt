package ir.amirreza.composepreferences.compositions

import android.content.SharedPreferences
import androidx.compose.runtime.compositionLocalOf

/**
 * A CompositionLocal for providing a SharedPreferences instance.
 * This allows accessing SharedPreferences in a Compose hierarchy.
 * If no value is provided, it defaults to null.
 */
val LocalSharedPreferences = compositionLocalOf<SharedPreferences?> {
    null
}