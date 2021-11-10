package yapp.android1.delibuddy.util

import android.content.Context
import android.content.Intent
import android.os.Bundle


inline fun <reified T>Context.intentTo(to: Class<T>, bundle: Bundle? = null) {
    val intent = Intent(this, to)
    startActivity(intent, bundle)
}