package yapp.android1.delibuddy.util.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceHelper {
    fun getDefaultPrefs(
        context: Context
    ): SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    private fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any) {
        when (value) {
            is String -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Double -> edit { it.putLong(key, value.toRawBits()) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Unsupported Operation")
        }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> SharedPreferences.get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> getString(key, defaultValue as String) as T
            is Int -> getInt(key, defaultValue as Int) as T
            is Boolean -> getBoolean(key, defaultValue) as T
            is Float -> getFloat(key, defaultValue) as T
            is Double -> Double.fromBits(getLong(key, defaultValue.toRawBits())) as T
            is Long -> getLong(key, defaultValue) as T
            else -> throw UnsupportedOperationException("Unsupported Operation")
        }
    }
}