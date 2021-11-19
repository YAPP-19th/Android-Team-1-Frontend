package yapp.android1.delibuddy.ui.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {
//    private val viewModel: LocationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
    }
}