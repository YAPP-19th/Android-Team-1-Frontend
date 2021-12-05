package yapp.android1.delibuddy.ui.partyInformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.CommunityViewPagerAdapter
import yapp.android1.delibuddy.databinding.ActivityPartyInformationBinding
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent.OnIntent
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class PartyInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartyInformationBinding

    private val viewModel by viewModels<PartyInformationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartyInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiverIntent()
        initializeView()
        collectData()
    }

    private fun receiverIntent() {
        val intentData = intent.getSerializableExtra("party") as Party
        viewModel.occurEvent(OnIntent(intentData))
    }

    private fun initializeView() = with(binding) {
        vpCommunity.adapter = CommunityViewPagerAdapter(this@PartyInformationActivity)

        TabLayoutMediator(tlCommunity, vpCommunity) { tab, position ->
            when(position) {
                0 -> tab.text = "댓글"
                1 -> tab.text = "파티인원"
            }
        }.attach()
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.party.collect { party ->
                renderView(party)
            }
        }
    }

    private fun renderView(party: Party) = with(binding) {

    }
}