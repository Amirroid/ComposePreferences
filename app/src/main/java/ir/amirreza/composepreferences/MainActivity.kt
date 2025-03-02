package ir.amirreza.composepreferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ir.amirreza.composepreferences.state.rememberPreferenceStateOf
import ir.amirreza.composepreferences.ui.theme.ComposePreferencesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePreferencesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        var isChecked: Boolean by rememberPreferenceStateOf("dark_mode", true)
                        Switch(
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = it
                            }
                        )
                    }
                }
            }
        }
    }
}