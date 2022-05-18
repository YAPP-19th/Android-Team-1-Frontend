package yapp.android1.delibuddy.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivityHomeBinding
import yapp.android1.delibuddy.ui.home.viewmodel.HomeViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        initObserve()
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        navController?.let { binding.bottomNavigation.setupWithNavController(it) }
    }

    private fun initObserve() {
        repeatOnStarted {
            homeViewModel.isAppTerminate.collect { isAppTerminate ->
                if (isAppTerminate) {
                    finishAffinity()
                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        R.string.home_back_button_once_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        binding.bottomNavigation.run {
            if (selectedItemId == R.id.homeFragment) {
                homeViewModel.occurEvent(HomeViewModel.HomeEvent.JudgeAppTerminate)
            } else {
                selectedItemId = R.id.homeFragment
            }
        }
    }
}
