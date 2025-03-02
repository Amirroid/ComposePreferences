package ir.amirreza.composepreferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.amirreza.composepreferences.models.User
import ir.amirreza.composepreferences.savers.UserPreferenceSaver
import ir.amirreza.composepreferences.state.rememberPreferenceStateOf
import java.util.Date

@Composable
fun HomeScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Boolean preference (Switch)
        var isChecked: Boolean by rememberPreferenceStateOf("dark_mode", true)
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )

        // String preference (TextField)
        var username: String by rememberPreferenceStateOf("username", "")
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        // Int preference (Slider)
        var volume: Int by rememberPreferenceStateOf("volume", 50)
        Column {
            Text("Volume: $volume")
            Slider(
                value = volume.toFloat(),
                onValueChange = { volume = it.toInt() },
                valueRange = 0f..100f
            )
        }

        // Float preference (Slider with decimal values)
        var brightness: Float by rememberPreferenceStateOf("brightness", 0.5f)
        Column {
            Text("Brightness: $brightness")
            Slider(
                value = brightness,
                onValueChange = { brightness = it },
                valueRange = 0f..1f
            )
        }

        // Long preference (Time Picker Simulation)
        val timestamp by rememberPreferenceStateOf("last_login", System.currentTimeMillis())
        Text("Last Login: ${Date(timestamp)}")


        var user by rememberPreferenceStateOf(
            "user",
            User("", ""),
            saver = UserPreferenceSaver
        )
        TextField(
            value = user.username,
            onValueChange = { user = user.copy(username = it) },
            label = { Text("Username") }
        )

        TextField(
            value = user.password,
            onValueChange = { user = user.copy(password = it) },
            label = { Text("Password") },
        )

        // Set<String> preference (Multiple Choice Selection)
        var selectedItems: Set<String> by rememberPreferenceStateOf("selected_items", setOf())
        Column {
            listOf("Item 1", "Item 2", "Item 3").forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        selectedItems = if (item in selectedItems) {
                            selectedItems - item
                        } else {
                            selectedItems + item
                        }
                    }
                ) {
                    Checkbox(
                        checked = item in selectedItems,
                        onCheckedChange = null
                    )
                    Text(item)
                }
            }
        }
    }
}

