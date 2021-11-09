package yapp.android1.delibuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivitySplashBinding

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_splash)

        // TODO: 서버 개발이 끝나면 이곳에서 데이터 수신 후 intent 예정

        Handler().postDelayed({
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}