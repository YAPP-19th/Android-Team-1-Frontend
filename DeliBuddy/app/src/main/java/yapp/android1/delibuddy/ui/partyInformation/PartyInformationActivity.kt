package yapp.android1.delibuddy.ui.partyInformation

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.CommunityViewPagerAdapter
import yapp.android1.delibuddy.databinding.ActivityPartyInformationBinding
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent.OnIntent
import yapp.android1.delibuddy.ui.partyInformation.view.AppBarStateChangeListener
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.extensions.show

@AndroidEntryPoint
class PartyInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartyInformationBinding

    private val viewModel by viewModels<PartyInformationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartyInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeView()
        initializeViewPager()
        initializeCollapsing()
        receiverIntent()
        collectData()
    }

    private fun receiverIntent() {
        val intentData = intent.getSerializableExtra("party") as Party
        viewModel.occurEvent(OnIntent(intentData))
    }

    private fun collectData() {
        repeatOnStarted {
            viewModel.isOwner.collect { isOwner ->
                switchViewState(isOwner)
            }
        }

        repeatOnStarted {
            viewModel.party.collect { party ->
                settingPartyInformationViews(party)
            }
        }
    }

    private fun settingPartyInformationViews(party: Party) = with(binding) {
        tvPartyLocation.text = party.coordinate
        tvPartyTitle.text    = party.title
        tvOrderTime.text     = party.orderTime
        tvPartyContent.text  = party.body
        tvStatus.text        = party.status

        toolbarContainer.tvTitle.text    = party.title
        toolbarContainer.tvLocation.text = "${party.placeName} ${party.placeNameDetail}"

        val backgroundColor = Color.parseColor("#${party.category.backgroundColorCode}")
        clBackground.setBackgroundColor(backgroundColor)
        window.statusBarColor = backgroundColor

        nestedScollView.setBackgroundColor(backgroundColor)

        Glide.with(this@PartyInformationActivity)
            .load(party.category.iconUrl)
            .into(ivPartyFoodType)

    }

    private fun initializeView() = with(binding) {
        toolbarContainer.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun switchViewState(isOwner: Boolean) = with(binding) {
        if(isOwner) {
            tvStatus.hide()
            tvStatusChange.show()
        } else {
            tvStatus.show()
            tvStatusChange.hide()
        }
    }

    private fun initializeCollapsing() = with(binding) {

        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    State.COLLAPSED -> {
                        toolbarContainer.tvTitle.visibility = View.VISIBLE
                        toolbarContainer.tvLocation.visibility = View.VISIBLE
                        toolbarContainer.tvTitle.animate().alpha(1F).setDuration(100)
                        toolbarContainer.tvLocation.animate().alpha(1F).setDuration(100)
                        clBackground.animate().alpha(0F).setDuration(100)
                    }
                    State.EXPANDED -> {
                        toolbarContainer.tvTitle.visibility = View.INVISIBLE
                        toolbarContainer.tvLocation.visibility = View.INVISIBLE
                        toolbarContainer.tvTitle.animate().alpha(0f).setDuration(250)
                        toolbarContainer.tvLocation.animate().alpha(0F).setDuration(250)
                        clBackground.animate().alpha(1F).setDuration(250)
                    }
                    State.IDLE -> Unit
                }
            }
        })
    }

    private fun initializeViewPager() = with(binding) {
        vpCommunity.adapter = CommunityViewPagerAdapter(this@PartyInformationActivity)

        TabLayoutMediator(tlCommunity, vpCommunity) { tab, position ->
            when(position) {
                0 -> tab.text = "댓글"
                1 -> tab.text = "파티인원"
            }
        }.attach()
    }

}