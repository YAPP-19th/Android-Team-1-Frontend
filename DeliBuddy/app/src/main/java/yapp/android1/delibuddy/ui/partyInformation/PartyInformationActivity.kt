package yapp.android1.delibuddy.ui.partyInformation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
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
import yapp.android1.delibuddy.ui.myparty.PARTY_INFORMATION
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Action
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Action.*
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Callback.CommentCallback
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Callback.PartyCallback
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus
import yapp.android1.delibuddy.ui.partyInformation.view.*
import yapp.android1.delibuddy.util.extensions.*
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

@AndroidEntryPoint
class PartyInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartyInformationBinding

    private val viewModel by viewModels<PartyInformationViewModel>()

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPartyInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeView()
        initializeViewPager()
        initializeCollapsing()
        receiveIntent()
        collectData()
    }

    private fun receiveIntent() {
        when {
            intent.getSerializableExtra("party") as? Party != null -> {
                val partyData = intent.getSerializableExtra("party") as Party
                viewModel.occurEvent(
                    IntentAction.OnPartyIntent(
                        data          = partyData,
                        currentUserId = sharedPreferencesManager.getUserId()
                    )
                )
            }

            intent.getSerializableExtra(PARTY_INFORMATION) as? PartyInformation != null -> {
                val partyInformation = intent.getSerializableExtra(PARTY_INFORMATION) as PartyInformation
                viewModel.occurEvent(
                    IntentAction.OnPartyInformationIntent(
                        data          = partyInformation,
                        currentUserId = sharedPreferencesManager.getUserId()
                    )
                )
            }
        }
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

    private fun handleEvent(event: PartyInformationViewModel.Callback) {
        when (event) {
            is PartyCallback.OnPartyJoinSuccess         -> { openOpenKakaoTalk(event.openKakaoTalkUrl) }
            is PartyCallback.OnPartyJoinFailed          -> { showToast("파티 인원이 다 찼습니다") }
            is PartyCallback.PartyDeleteSuccess         -> { finish() }
            is PartyCallback.PartyDeleteFailed          -> { showToast("파티 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요") }
            is CommentCallback.OnCreateCommentSuccess   -> { handleWriteCommentSuccess() }
            is CommentCallback.OnCreateCommentFailed    -> { showToast("댓글 작성에 실패했습니다 다시 시도해 주세요") }
            is CommentCallback.ShowTargetParentComment  -> { showTargetComment(event.parentComment) }
            is CommentCallback.HideTargetParentComment  -> { hideTargetComment() }
            is CommentCallback.CommentDeleteSuccess     -> { showToast("댓글이 성공적으로 삭제됐습니다") }
            is CommentCallback.CommentDeleteFailed      -> { showToast("댓글 삭제에 실패했습니다. 잠시 후 다시 시도해 주세요") }
            else -> Unit
        }
    }

    private fun handleWriteCommentSuccess() = with(binding) {
        etInputComment.setText("")
        showToast("댓글이 정상적으로 등록되었습니다")
        etInputComment.hideKeyboard()
        hideTargetComment()
    }

    private fun showTargetComment(parentComment: Comment) = with(binding) {
        tvParentCommentWriter.text = parentComment.writer?.nickName + " 님에게 답장"
        tvParentCommentBody.text   = parentComment.body
        showTargetCommentAnimation()

        etInputComment.showKeyboard()
    }

    private fun hideTargetComment() = with(binding) {
        tvParentCommentWriter.text = ""
        tvParentCommentBody.text   = ""
        hideTargetCommentAnimation()
    }

    private fun showTargetCommentAnimation() = with(binding) {
        ObjectAnimator.ofFloat(
            clTargetCommentContainer,
            View.TRANSLATION_Y,
            0F,
            -clTargetCommentContainer.height.toFloat()
        ).apply {
            interpolator = BounceInterpolator()
            duration = 500L

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) = Unit

                override fun onAnimationEnd(animation: Animator?) = Unit

                override fun onAnimationRepeat(animation: Animator?) = Unit

                override fun onAnimationCancel(animation: Animator?) = Unit
            })
        }.start()
    }

    private fun hideTargetCommentAnimation() = with(binding) {
        ObjectAnimator.ofFloat(
            clTargetCommentContainer,
            View.TRANSLATION_Y,
            -clTargetCommentContainer.height.toFloat(),
            0F
        ).apply {
            interpolator = BounceInterpolator()
            duration = 500L

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    viewModel.occurEvent(CommentAction.DeleteTargetComment)
                }

                override fun onAnimationEnd(animation: Animator?) = Unit

                override fun onAnimationRepeat(animation: Animator?) = Unit

                override fun onAnimationCancel(animation: Animator?) = Unit
            })
        }.start()
    }

    private fun setEnableJoinButton() = with(binding) {
        btnJoinParty.text = "참가중"
    }

    private fun setDisableJoinButton() = with(binding) {
        btnJoinParty.text = "파티 참가"
    }

    private fun settingPartyInformationViews(party: PartyInformation) = with(binding) {
        // [Header]
        tvPartyLocation.text = "${party.placeName} \n${party.placeNameDetail}"
        tvPartyTitle.text    = party.title
        tvPartyContent.text  = party.body

        tvOrderTime.text     = party.orderTime + " 주문 예정"
        val span = tvOrderTime.text as Spannable
        span.setSpan(
            ForegroundColorSpan(getColor(R.color.text_grey)),
            tvOrderTime.text.lastIndex - 4,
            tvOrderTime.text.lastIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (party.isIn) {
            setEnableJoinButton()
        } else {
            setDisableJoinButton()
        }

        Glide.with(this@PartyInformationActivity)
            .load(party.category.iconUrl)
            .into(ivPartyFoodType)

        // [PartyStatus]
        tvStatus.backgroundTintList = when (party.status) {
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
        tvStatusChange.text = party.status.value

        // [ Toolbar ]
        toolbarContainer.tvTitle.text = party.title
        toolbarContainer.tvLocation.text = "${party.placeName} ${party.placeNameDetail}"

        // [Party Owner]
        tvPartyOwnerName.text = party.leader.nickName

        tvPartyOwnerPartiesCount.text    = "버디와 함께한 식사 ${party.leader.partiesCnt}번"
        val tvPartyOwnerPartiesCountSpan = tvPartyOwnerPartiesCount.text as Spannable
        tvPartyOwnerPartiesCountSpan.setSpan(
            StyleSpan(Typeface.BOLD),
            11,
            tvPartyOwnerPartiesCount.text.lastIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        Glide.with(this@PartyInformationActivity)
            .load(party.leader.profileImage)
            .into(ivPartyOwnerProfile)

        // [PartyInformation Background]
        val backgroundColor = Color.parseColor("#${party.category.backgroundColorCode}")
        clBackground.setBackgroundColor(backgroundColor)
        window.statusBarColor = backgroundColor
        nestedScollView.setBackgroundColor(backgroundColor)
    }

    @SuppressLint("ClickableViewAccessibility")
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
            viewModel.occurEvent(PartyAction.OnJointPartyClicked)
        }

        btnCreateComment.setOnClickListener {
            if (etInputComment.text.toString().trim() != "") {
                viewModel.occurEvent(CommentAction.WriteComment(etInputComment.text.toString()))
            } else {
                showToast("댓글 내용을 입력해주세요")
            }
        }

        etInputComment.setOnClickListener {
            it.showKeyboard()
        }

        toolbarContainer.btnMoreOptions.setOnClickListener { optionsButton ->
            val editButton   = optionsMenuBalloon.getContentView().findViewById<ConstraintLayout>(R.id.btn_edit)
            val removeButton = optionsMenuBalloon.getContentView().findViewById<ConstraintLayout>(R.id.btn_remove)

            editButton.setOnClickListener {
                optionsMenuBalloon.dismiss()
                partyEditContract.launch(viewModel.party.value)
            }

            removeButton.setOnClickListener {
                optionsMenuBalloon.dismiss()
                showCustomDialog(
                    title          = "경고",
                    message        = "정말 해당 파티를 삭제하시겠습니까?",
                    positiveMethod = { viewModel.occurEvent(PartyAction.OnDeletePartyMenuClicked) },
                    negativeMethod = null
                )
            }

            optionsMenuBalloon.showAlignBottom(optionsButton)
        }

        clTargetCommentContainer.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideTargetCommentAnimation()
                }
                else -> Unit
            }

            true
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
            when (position) {
                0 -> tab.text = "댓글"
                1 -> tab.text = "파티인원"
            }
        }.attach()
    }

    private fun openOpenKakaoTalk(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

}
