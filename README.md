# 📌 ComposePreferences    [![](https://jitpack.io/v/Amirroid/ComposePreferences.svg)](https://jitpack.io/#Amirroid/ComposePreferences)

**ComposePreferences** is a Jetpack Compose library that enables direct state management with SharedPreferences.

---

## 🚀 Installation

### **Step 1. Add the repository**
Add the following to your `settings.gradle` file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### **Step 2. Add the dependency**

```gradle
dependencies {
    implementation 'com.github.Amirroid:ComposePreferences:last-version'
}
```

---

## 📖 Usage

### **Basic Example**

```kotlin
var isDarkModeEnabled by rememberPreferenceStateOf("dark_mode", true)
SettingItem(title = "Dark Mode") {
    Switch(
        checked = isDarkModeEnabled,
        onCheckedChange = { isDarkModeEnabled = it },
    )
}
```

### **Function Signature**

```kotlin
rememberPreferenceStateOf(
    key: String,
    defaultValue: T,
    sharedPreferences: SharedPreferences = LocalSharedPreferences.current ?: defaultPreferences(),
    saver: PreferenceSaver<T>? = null,
    vararg keys: Any
)
```

### **Parameter Explanation**
- `key: String` → The key used to store the value in SharedPreferences.
- `defaultValue: T` → The default value returned if the key does not exist.
- `sharedPreferences: SharedPreferences` → The SharedPreferences instance to use (default: a SharedPreferences instance named "screen").
- `saver: PreferenceSaver<T>?` → Custom saver for complex types (optional).
- `keys: Any` → Additional keys to trigger recomposition when changed.

### **Default Supported Types**
If `saver` is **not provided**, the following types are supported:

- String
- Int
- Boolean
- Float
- Long
- Set<String>
- Double

---

## ✨ Creating a Custom Saver
For complex data types, you can create a custom **PreferenceSaver**. Below is an example of saving and retrieving a `User` object:

```kotlin
@Immutable
data class User(
    val username: String,
    val password: String
)

object UserPreferenceSaver : PreferenceSaver<User> {
    private const val KEY: String = "user"

    override fun save(value: User, sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            putString("$KEY-username", value.username)
            putString("$KEY-password", value.password)
            apply()
        }
    }

    override fun get(sharedPreferences: SharedPreferences): User {
        val username = sharedPreferences.getString("$KEY-username", "") ?: ""
        val password = sharedPreferences.getString("$KEY-password", "") ?: ""
        return User(username, password)
    }
}
```

### **Usage in Compose**

```kotlin
var user by rememberPreferenceStateOf(
    "user",
    User("", ""),
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
```

### **Explanation**
- `save(value: User, sharedPreferences: SharedPreferences)`: Saves the `User` object by storing its properties separately.
- `get(sharedPreferences: SharedPreferences)`: Retrieves the `User` object by reading its properties from SharedPreferences.
- `rememberPreferenceStateOf(...)`: Uses `UserPreferenceSaver` to automatically store and retrieve `User` data in Compose.

This approach allows you to persist and manage complex data types seamlessly within your Jetpack Compose application.


---

## 🔧 Customizing SharedPreferences
You can provide a custom SharedPreferences instance using **CompositionLocalProvider**:

```kotlin
CompositionLocalProvider(
    LocalSharedPreferences provides preferences
) {
    content()
}
```

This allows you to override the default preference storage location within a specific Compose scope.
