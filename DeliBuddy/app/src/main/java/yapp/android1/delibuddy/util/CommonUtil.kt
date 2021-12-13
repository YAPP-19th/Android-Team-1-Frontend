package yapp.android1.delibuddy.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics


inline fun <reified T> Context.intentTo(to: Class<T>, bundle: Bundle? = null) {
    val intent = Intent(this, to)
    startActivity(intent, bundle)
}

inline fun Context.dpToPx(dp: Int): Float {
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}