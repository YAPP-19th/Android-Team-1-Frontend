package yapp.android1.delibuddy.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.TestViewModel
import yapp.android1.delibuddy.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<TestViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewListener()
        collectState()
    }

    private fun initializeViewListener() = with(binding) {
        btnIncrease.setOnClickListener { viewModel.occurEvent(TestViewModel.TestEvent.OnIncreaseClicked) }
        btnDecrease.setOnClickListener { viewModel.occurEvent(TestViewModel.TestEvent.OnDecreaseClicked) }
    }

    private fun collectState() = with(viewModel) {
        lifecycleScope.launchWhenStarted {
            number.collect { binding.tvNumber.text = it.toString() }
        }
    }
}