# 📌 ComposePreferences

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