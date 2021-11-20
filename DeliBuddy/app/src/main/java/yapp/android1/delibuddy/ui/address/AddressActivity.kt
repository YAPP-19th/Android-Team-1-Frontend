package yapp.android1.delibuddy.ui.address

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {
    //    private val viewModel: LocationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
    }
}