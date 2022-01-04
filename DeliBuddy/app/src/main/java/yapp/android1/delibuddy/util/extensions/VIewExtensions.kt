package yapp.android1.delibuddy.util.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


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