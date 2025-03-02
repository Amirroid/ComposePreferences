package ir.amirreza.composepreferences

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import ir.amirreza.composepreferences.compositions.LocalSharedPreferences
import ir.amirreza.composepreferences.ui.theme.ComposePreferencesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferences = getSharedPreferences("custom_screen", Context.MODE_PRIVATE)
        setContent {
            ComposePreferencesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    CompositionLocalProvider(
                        LocalSharedPreferences provides preferences
                    ) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}