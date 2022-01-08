package yapp.android1.delibuddy.ui.partyInformation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.CommunityViewPagerAdapter
import yapp.android1.delibuddy.databinding.ActivityPartyInformationBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.CommentType
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationAction
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationAction.OnIntent
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus
import yapp.android1.delibuddy.ui.partyInformation.view.*
import yapp.android1.delibuddy.ui.partyInformation.view.CommentOptionsBalloonFactory
import yapp.android1.delibuddy.ui.partyInformation.view.OptionsMenuBalloonFactory
import yapp.android1.delibuddy.ui.partyInformation.view.StatusBottomSheetDialog
import yapp.android1.delibuddy.util.extensions.*
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

@AndroidEntryPoint
class PartyInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartyInformationBinding

    private val optionsMenuBalloon by balloon<OptionsMenuBalloonFactory>()

    private val viewModel by viewModels<PartyInformationViewModel>()

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    private val partyEditContract = registerForActivityResult(PartyInformationContract()) { resultAction ->
        viewModel.occurEvent(resultAction)
    }

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
        viewModel.occurEvent(OnIntent(intentData, sharedPreferencesManager.getUserId()))
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

        lifecycleScope.launch {
            viewModel.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: PartyInformationEvent) {
        when(event) {
            is PartyInformationEvent.OnPartyJoinSuccess -> {
                Toast.makeText(this, "파티 참가 성공", Toast.LENGTH_SHORT).show()
            }

            is PartyInformationEvent.OnPartyJoinFailed -> {
                Toast.makeText(this, "파티 인원이 다 찼습니다.", Toast.LENGTH_SHORT).show()
            }

            is PartyInformationEvent.OnCreateCommentSuccess -> {
                binding.etInputComment.setText("")
                Toast.makeText(this, "댓글이 정상적으로 등록되었습니다", Toast.LENGTH_SHORT).show()

                binding.etInputComment.hideKeyboard()
                hideTargetComment()
            }

            is PartyInformationEvent.OnCreateCommentFailed -> {
                Toast.makeText(this, "댓글 작성에 실패했습니다 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
            }

            is PartyInformationEvent.ShowTargetParentComment -> { showTargetComment(event.parentComment) }

            is PartyInformationEvent.HideTargetParentComment -> { hideTargetComment() }

            is PartyInformationEvent.PartyDeleteSuccess -> { finish() }

            is PartyInformationEvent.PartyDeleteFailed -> {
                Toast.makeText(this, "파티 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

            is PartyInformationEvent.CommentDeleteSuccess -> {
                Toast.makeText(this, "댓글이 성공적으로 삭제됐습니다.", Toast.LENGTH_SHORT).show()
            }

            is PartyInformationEvent.CommentDeleteFailed -> {
                Toast.makeText(this, "댓글 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    private fun showTargetComment(parentComment: Comment) = with(binding) {
        tvParentCommentWriter.text = parentComment.writer?.nickName + " 님에게 답장"
        tvParentCommentBody.text = parentComment.body
        clTargetCommentContainer.show()

        etInputComment.showKeyboard()
    }

    private fun hideTargetComment() = with(binding) {
        tvParentCommentWriter.text = ""
        tvParentCommentBody.text = ""
        clTargetCommentContainer.hide()
    }

    private fun setEnableJoinButton() = with(binding) {
        btnJoinParty.text = "참가중"
    }

    private fun setDisableJoinButton() = with(binding) {
        binding.btnJoinParty.text = "파티 참가"
    }

    private fun settingPartyInformationViews(party: PartyInformation) = with(binding) {
        // [Header]
        tvPartyLocation.text = "${party.placeName} \n${party.placeNameDetail}"
        tvPartyTitle.text    = party.title
        tvPartyContent.text  = party.body

        tvOrderTime.text     = party.orderTime + " 주문 예정"
        val span = tvOrderTime.text as Spannable
        span.setSpan(ForegroundColorSpan(getColor(R.color.text_grey)), tvOrderTime.text.lastIndex - 4, tvOrderTime.text.lastIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        if(party.isIn) {
            setEnableJoinButton()
        } else {
            setDisableJoinButton()
        }

        Glide.with(this@PartyInformationActivity)
            .load(party.category.iconUrl)
            .into(ivPartyFoodType)

        // [PartyStatus]
        tvStatus.backgroundTintList = when(party.status) {
            PartyStatus.RECRUIT -> {
                 ContextCompat.getColorStateList(this@PartyInformationActivity, R.color.sub_yellow)
            }
            PartyStatus.ORDER -> {
                ContextCompat.getColorStateList(this@PartyInformationActivity, R.color.sub_purple)
            }
            PartyStatus.COMPLETED -> {
                ContextCompat.getColorStateList(this@PartyInformationActivity, R.color.sub_grey)
            }
        }

        tvStatus.text = party.status.value
        tvStatusChange.text  = party.status.value

        // [ Toolbar ]
        toolbarContainer.tvTitle.text    = party.title
        toolbarContainer.tvLocation.text = "${party.placeName} ${party.placeNameDetail}"

        // [Party Owner]
        tvPartyOwnerName.text = party.leader.nickName

        tvPartyOwnerPartiesCount.text = "버디와 함께한 식사 ${party.leader.partiesCnt}번"
        val tvPartyOwnerPartiesCountSpan = tvPartyOwnerPartiesCount.text as Spannable
        tvPartyOwnerPartiesCountSpan.setSpan(StyleSpan(Typeface.BOLD), 11, tvPartyOwnerPartiesCount.text.lastIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        Glide.with(this@PartyInformationActivity)
            .load(party.leader.profileImage)
            .into(ivPartyOwnerProfile)

        // [PartyInformation Background]
        val backgroundColor = Color.parseColor("#${party.category.backgroundColorCode}")
        clBackground.setBackgroundColor(backgroundColor)
        window.statusBarColor = backgroundColor
        nestedScollView.setBackgroundColor(backgroundColor)
    }

    private fun initializeView() = with(binding) {
        toolbarContainer.btnBack.setOnClickListener {
            onBackPressed()
        }

        tvStatusChange.setOnClickListener {
            supportFragmentManager.let { fragmentManager ->
                val bottomSheetDialog = StatusBottomSheetDialog()
                bottomSheetDialog.show(fragmentManager, null)
            }
        }

        btnJoinParty.setOnClickListener {
            //viewModel.occurEvent(PartyInformationAction.OnJointPartyClicked)
            //makeKakaoMessage()

            openKakaoLink()
        }

        btnCreateComment.setOnClickListener {
            if(etInputComment.text.toString() != "") {
                viewModel.occurEvent(PartyInformationAction.WriteComment(etInputComment.text.toString()))
            } else {
                Toast.makeText(this@PartyInformationActivity, "댓글 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        etInputComment.setOnClickListener {
            it.showKeyboard()
        }

        toolbarContainer.btnMoreOptions.setOnClickListener { optionsButton ->
            val editButton = optionsMenuBalloon.getContentView().findViewById<ConstraintLayout>(R.id.btn_edit)
            val removeButton = optionsMenuBalloon.getContentView().findViewById<ConstraintLayout>(R.id.btn_remove)

            editButton.setOnClickListener {
                optionsMenuBalloon.dismiss()
                partyEditContract.launch(viewModel.party.value)
            }

            removeButton.setOnClickListener {
                optionsMenuBalloon.dismiss()
                viewModel.occurEvent(PartyInformationAction.OnDeletePartyMenuClicked)
            }

            optionsMenuBalloon.showAlignBottom(optionsButton)
        }
    }

    private fun switchViewState(isOwner: Boolean) = with(binding) {
        if(!isOwner) {
            toolbarContainer.btnMoreOptions.hide()
            tvStatus.show()
            tvStatusChange.hide()
        } else {
            toolbarContainer.btnMoreOptions.show()
            tvStatus.hide()
            tvStatusChange.show()
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

    private fun openKakaoLink() {
        val browseLink = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/gPA7PgTd"))
        startActivity(browseLink)
    }

    private fun makeKakaoMessage() {
        val text = TextTemplate(
            text = """
                ${viewModel.party.value.openKakaoUrl} 에 참가하세
            """.trimIndent(),
            link = Link(
                webUrl = "https://open.kakao.com/o/gPA7PgTd",
                mobileWebUrl = "https://open.kakao.com/o/gPA7PgTd"
            )
        )

        val TAG = "[TAG]"

        if (LinkClient.instance.isKakaoLinkAvailable(this)) {
            // 카카오톡으로 카카오링크 공유 가능
            LinkClient.instance.defaultTemplate(this, text) { linkResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오링크 보내기 실패", error)
                }
                else if (linkResult != null) {
                    Log.d(TAG, "카카오링크 보내기 성공 ${linkResult.intent}")
                    startActivity(linkResult.intent)

                    // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${linkResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${linkResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.defaultTemplateUri(text)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabs으로 Chrome 브라우저 열기
            try {
                KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // Chrome 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabs으로 디바이스 기본 브라우저 열기
            try {
                KakaoCustomTabsClient.open(this, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }

}