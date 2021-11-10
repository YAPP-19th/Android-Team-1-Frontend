package yapp.android1.delibuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivitySplashBinding
import yapp.android1.delibuddy.util.intentTo

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_splash)

        // TODO: 서버 개발이 끝나면 이곳에서 데이터 수신 후 intent 예정
        lifecycleScope.launch {
            delay(2000L)
            intentTo(PermissionActivity::class.java)
        }
    }
}