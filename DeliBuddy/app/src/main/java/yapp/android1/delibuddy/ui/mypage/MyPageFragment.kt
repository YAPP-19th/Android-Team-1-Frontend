package yapp.android1.delibuddy.ui.mypage

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.activity.viewModels
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentMypageBinding
import yapp.android1.delibuddy.ui.myparty.adapter.MyPartyMockGenerator
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(
    FragmentMypageBinding::inflate
) {
    private val viewModel by viewModels<MyPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectData()
    }

    private fun initViews() {
        binding.tvLicense.setOnClickListener {
            requireContext().intentTo(OssLicensesMenuActivity::class.java)
        }
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.myInfo.collect {
                binding.tvUsername.text = it?.nickname
                binding.tvBannerTitle.text =
                    SpannableStringBuilder()
                        .append(getString(R.string.navigation_mypage_banner_title))
                        .append(" ")
                        .bold {
                            append(getString(R.string.navigation_mypage_count, it?.partiesCnt ?: 0))
                        }

                Glide.with(binding.ivProfile)
                    .load(it?.profileImage)
                    .into(binding.ivProfile)
            }

            viewModel.currentAddress.collect {
                binding.tvLocation.text = it?.addressName
            }
        }
    }
}
