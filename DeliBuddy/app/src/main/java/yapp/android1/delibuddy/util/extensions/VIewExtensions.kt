package yapp.android1.delibuddy.util.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import yapp.android1.delibuddy.R


fun View.show() {
    if(this.visibility == View.INVISIBLE || this.visibility == View.GONE) {
        this.visibility = View.VISIBLE
    }
}

fun View.hide() {
    if(this.visibility == View.VISIBLE) {
        this.visibility = View.INVISIBLE
    }
}

fun View.gone() {
    if(this.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    }
}

fun View.showKeyboard(isForced: Boolean = false) {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, if (isForced) InputMethodManager.SHOW_FORCED else InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.showToast(body: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, body, duration).show()
}

fun Context.showCustomDialog(title: String, message: String, positiveMethod: () -> Unit, negativeMethod: (() -> Unit)?) {
    MaterialAlertDialogBuilder(this, R.style.MaterialDialog_DeliBuddy_Style)
        .setTitle(title)
        .setIcon(R.drawable.icon_delibuddy_foreground)
        .setMessage(message)
        .setPositiveButton("확인") { _, _ -> positiveMethod.invoke() }
        .setNegativeButton("취소") { _, _ -> negativeMethod?.invoke()}
        .show()
}