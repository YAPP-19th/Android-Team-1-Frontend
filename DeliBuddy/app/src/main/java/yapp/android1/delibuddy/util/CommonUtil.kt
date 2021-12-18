package yapp.android1.delibuddy.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import java.text.SimpleDateFormat

const val INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val OUTPUT_DATE_FORMAT = "MM월 dd일 HH시 mm분"

inline fun <reified T> Context.intentTo(to: Class<T>, bundle: Bundle? = null) {
    val intent = Intent(this, to)
    startActivity(intent, bundle)
}

inline fun Context.dpToPx(dp: Int): Float {
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

inline fun parseDate(dateStr: String): String {
    val inputDateFormatter = SimpleDateFormat(INPUT_DATE_FORMAT)
    val outputDateFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT)

    var date = inputDateFormatter.parse(dateStr)
    return outputDateFormatter.format(date)
}