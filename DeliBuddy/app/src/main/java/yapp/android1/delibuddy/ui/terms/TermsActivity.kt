package yapp.android1.delibuddy.ui.terms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {

    companion object {
        const val TERMS_WEBVIEW_BASEURL = "file:///android_asset/"

        const val EXTRA_TERMS = "yapp.android1.delibuddy.ui.terms"
        const val FILE_TERMS_OF_SERVICE = 0
        const val PRIVACY_POLICY = 1
    }

    val termsInformation = listOf<Pair<Int, String>>(
        Pair(R.string.policy_info_terms_of_service, "policyInfoTermsOfService.html"),
        Pair(R.string.policy_info_privacy_policy, "policyInfoPrivacyPolicy.html")
    )

    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var termsIndex = intent.getIntExtra(EXTRA_TERMS, -1)

        initView(termsIndex)
    }

    private fun initView(index: Int) {
        binding.title.text = getString(termsInformation[index].first)

        binding.webview.loadUrl(TERMS_WEBVIEW_BASEURL + termsInformation[index].second)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}