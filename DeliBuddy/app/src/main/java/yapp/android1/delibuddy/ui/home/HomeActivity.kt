package yapp.android1.delibuddy.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivityHomeBinding
import yapp.android1.delibuddy.ui.alarm.AlarmFragment
import yapp.android1.delibuddy.ui.home.fragments.HomeFragment
import yapp.android1.delibuddy.ui.home.viewmodel.HomeViewModel
import yapp.android1.delibuddy.ui.mypage.MyPageFragment
import yapp.android1.delibuddy.ui.myparty.MyPartyFragment
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val homeFragment by lazy { HomeFragment() }
    private val myPartyFragment by lazy { MyPartyFragment() }
    private val alarmFragment by lazy { AlarmFragment() }
    private val myPageFragment by lazy { MyPageFragment() }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null)
            initBottomNavigation()
        changeBottomNavigationItemByClick()
        initObserve()
    }

    private fun initObserve() {
        repeatOnStarted {
            homeViewModel.isAppTermiate.collect { isAppTerminate ->
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

    private fun initBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.home

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container, homeFragment)
        }
    }

    private fun changeBottomNavigationItemByClick() {
        binding.bottomNavigation.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> changeFragment(homeFragment, R.string.navigation_home)
                    R.id.myparty -> changeFragment(myPartyFragment, R.string.navigation_myparty)
                    R.id.alarm -> changeFragment(alarmFragment, R.string.navigation_alarm)
                    R.id.mypage -> changeFragment(myPageFragment, R.string.navigation_mypage)
                }
                true
            }
        }
    }

    private fun changeFragment(fragment: Fragment, nameId: Int) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack(resources.getString(nameId))
        }
    }

    override fun onBackPressed() {
        binding.bottomNavigation.run {
            if (selectedItemId == R.id.home) {
                homeViewModel.occurEvent(HomeViewModel.HomeEvent.JudgeAppTerminate())
            } else {
                selectedItemId = R.id.home
            }
        }
    }
}
