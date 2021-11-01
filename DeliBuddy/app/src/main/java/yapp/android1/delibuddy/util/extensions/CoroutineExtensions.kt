package yapp.android1.delibuddy.util.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    when (this) {
        is Activity -> {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
            }
        }
        is Fragment -> {
            lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
            }
        }
    }
}