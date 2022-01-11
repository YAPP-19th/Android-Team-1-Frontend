package yapp.android1.delibuddy.util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

const val INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val INPUT_COMMENT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
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

inline fun parseCommentDate(dateString: String):String {
    val inputDateFormatter = SimpleDateFormat(INPUT_COMMENT_DATE_FORMAT)
    val outputDateFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT)

    val date = inputDateFormatter.parse(dateString)
    return outputDateFormatter.format(date)
}