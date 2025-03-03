package ir.amirreza.composepreferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.amirreza.composepreferences.compositions.LocalSharedPreferences
import ir.amirreza.composepreferences.models.User
import ir.amirreza.composepreferences.savers.UserPreferenceSaver
import ir.amirreza.composepreferences.state.rememberPreferenceStateOf
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text("Compose Preferences")
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            // ✅ Boolean preference (Dark Mode switch)
            var isDarkModeEnabled by rememberPreferenceStateOf("dark_mode", true)
            SettingItem(title = "Dark Mode") {
                Switch(
                    checked = isDarkModeEnabled,
                    onCheckedChange = { isDarkModeEnabled = it },
                )
            }
            var isDarkModeEnabled2 by rememberPreferenceStateOf("dark_mode", true)
            SettingItem(title = "Dark Mode 2") {
                Switch(
                    checked = isDarkModeEnabled2,
                    onCheckedChange = { isDarkModeEnabled2 = it },
                )
            }

            // ✅ String preference (Username)
            var username by rememberPreferenceStateOf("username", "")
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ Int preference (Volume control)
            var volume by rememberPreferenceStateOf("volume", 50)
            SettingItem(title = "Volume: $volume") {
                Slider(
                    value = volume.toFloat(),
                    onValueChange = { volume = it.toInt() },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ✅ Double preference (Custom Range Example: 0.0 - 10.0)
            var customValue by rememberPreferenceStateOf("custom_double", 5.0)
            SettingItem(title = "Custom Value: ${"%.2f".format(customValue)}") {
                Slider(
                    value = customValue.toFloat(),
                    onValueChange = { customValue = it.toDouble() },
                    valueRange = 0f..10f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ✅ Float preference (Brightness control)
            var brightness by rememberPreferenceStateOf("brightness", 0.5f)
            SettingItem(title = "Brightness: $brightness") {
                Slider(
                    value = brightness,
                    onValueChange = { brightness = it },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ✅ Long preference (Last Login Timestamp)
            var lastLoginTimestamp by rememberPreferenceStateOf(
                "last_login",
                System.currentTimeMillis()
            )
            SettingItem(title = "Last Login") {
                Text(Date(lastLoginTimestamp).toString(), modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    lastLoginTimestamp = System.currentTimeMillis()
                }) {
                    Text("Save current time")
                }
            }

            // ✅ Custom object preference (`User`)
            var user by rememberPreferenceStateOf(
                defaultValue = User("", ""),
                saver = UserPreferenceSaver
            )

            OutlinedTextField(
                value = user.username,
                onValueChange = { user = user.copy(username = it) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = user.password,
                onValueChange = { user = user.copy(password = it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ Set<String> preference (Multi-selection items)
            var selectedItems by rememberPreferenceStateOf("selected_items", setOf<String>())

            SettingItem(title = "Select Items") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    listOf("Item 1", "Item 2", "Item 3").forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedItems = if (item in selectedItems) {
                                        selectedItems - item
                                    } else {
                                        selectedItems + item
                                    }
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = item in selectedItems,
                                onCheckedChange = null
                            )
                            Text(item, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * A reusable composable for setting items with a title and a customizable content.
 */
@Composable
fun SettingItem(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}