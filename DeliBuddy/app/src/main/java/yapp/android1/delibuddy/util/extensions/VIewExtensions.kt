package yapp.android1.delibuddy.util.extensions

import android.view.View


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