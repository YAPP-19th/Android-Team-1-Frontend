package yapp.android1.delibuddy.ui.address

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import yapp.android1.delibuddy.R

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
    }

    companion object {
        const val ADDRESS_ACTIVITY_RESULT_CODE = 1000
        const val ADDRESS_ACTIVITY_USER_ADDRESS = "USER_ADDRESS"
    }
}
