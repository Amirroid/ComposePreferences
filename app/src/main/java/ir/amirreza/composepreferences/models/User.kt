package ir.amirreza.composepreferences.models

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val username: String,
    val password: String
)