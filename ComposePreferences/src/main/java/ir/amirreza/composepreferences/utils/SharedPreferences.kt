package ir.amirreza.composepreferences.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable

internal val Context.defaultPreferences: SharedPreferences
    get() = getSharedPreferences("screen", Context.MODE_PRIVATE)

@Composable
internal fun defaultPreferences() = LocalContext.current.defaultPreferences