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
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.myparty.PARTY_INFORMATION
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Action.*
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Callback.CommentCallback
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Callback.PartyCallback
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus
import yapp.android1.delibuddy.ui.partyInformation.view.*
import yapp.android1.delibuddy.ui.splash.SplashActivity
import yapp.android1.delibuddy.util.Constants.NOT_FOUND
import yapp.android1.delibuddy.util.extensions.*
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

@AndroidEntryPoint
class PartyInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartyInformationBinding

    private val viewModel by viewModels<PartyInformationViewModel>()

    private val optionsMenuBalloonForOwner by balloon<OptionsMenuBalloonForOwnerFactory>()
    private val optionsMenuBalloonForUser by balloon<OptionsMenuBalloonForUserFactory>()

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    private val partyEditContract =
        registerForActivityResult(PartyInformationContract()) { resultAction ->
            viewModel.occurEvent(resultAction)
        }

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
                val partyInformation =
                    intent.getSerializableExtra(PARTY_INFORMATION) as PartyInformation
                viewModel.occurEvent(
                    IntentAction.OnPartyInformationIntent(
                        data          = partyInformation,
                        currentUserId = sharedPreferencesManager.getUserId()
                    )
                )
            }

            else -> {
                val partyId = intent.getIntExtra(SplashActivity.KEY_PARTY_ID, -1)
                if (partyId != NOT_FOUND) {
                    viewModel.occurEvent(
                        IntentAction.OnPartyIdIntent(
                            partyId       = partyId,
                            currentUserId = sharedPreferencesManager.getUserId()
                        )
                    )
                }
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
            is PartyCallback.OnPartyJoinFailed          -> { showToast("?????? ????????? ??? ????????????") }
            is PartyCallback.PartyDeleteSuccess         -> { finish() }
            is PartyCallback.PartyDeleteFailed          -> { showToast("?????? ????????? ??????????????????. ?????? ??? ?????? ????????? ?????????") }
            is PartyCallback.PartyLeaveSuccess          -> { finish() }
            is PartyCallback.PartyLeaveFailed           -> { showToast("?????? ???????????? ??????????????????. ?????? ??? ?????? ????????? ?????????") }
            is CommentCallback.OnCreateCommentSuccess   -> { handleWriteCommentSuccess() }
            is CommentCallback.OnCreateCommentFailed    -> { showToast("?????? ????????? ?????????????????? ?????? ????????? ?????????") }
            is CommentCallback.ShowTargetParentComment  -> { showTargetComment(event.parentComment) }
            is CommentCallback.HideTargetParentComment  -> { hideTargetComment() }
            is CommentCallback.CommentDeleteSuccess     -> { showToast("????????? ??????????????? ??????????????????") }
            is CommentCallback.CommentDeleteFailed      -> { showToast("?????? ????????? ??????????????????. ?????? ??? ?????? ????????? ?????????") }
            else -> Unit
        }
    }

    private fun handleWriteCommentSuccess() = with(binding) {
        etInputComment.setText("")
        showToast("????????? ??????????????? ?????????????????????")
        etInputComment.hideKeyboard()
        hideTargetComment()
    }

    private fun showTargetComment(parentComment: Comment) = with(binding) {
        tvParentCommentWriter.text = parentComment.writer?.nickName + " ????????? ??????"
        tvParentCommentBody.text = parentComment.body
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
        btnJoinParty.text = "?????????"
    }

    private fun setDisableJoinButton() = with(binding) {
        btnJoinParty.text = "?????? ??????"
    }

    private fun settingPartyInformationViews(party: PartyInformation) = with(binding) {
        // [Header]
        tvPartyLocation.text = "${party.placeName} \n${party.placeNameDetail}"
        tvPartyTitle.text = party.title
        tvPartyContent.text = party.body

        tvOrderTime.text = party.orderTime + " ?????? ??????"
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

        tvStatus.text       = party.status.value
        tvStatusChange.text = party.status.value

        // [ Toolbar ]
        toolbarContainer.tvTitle.text    = party.title
        toolbarContainer.tvLocation.text = "${party.placeName} ${party.placeNameDetail}"

        // [Party Owner]
        tvPartyOwnerName.text = party.leader.nickName

        tvPartyOwnerPartiesCount.text    = "????????? ????????? ?????? ${party.leader.partiesCnt}???"
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
                showToast("?????? ????????? ??????????????????")
            }
        }

        etInputComment.setOnClickListener {
            it.showKeyboard()
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
        if (!isOwner) {
            tvStatus.show()
            tvStatusChange.hide()
            setUserOptionsButtonListener()
        } else {
            tvStatus.hide()
            tvStatusChange.show()
            setOwnerOptionsButtonListener()
        }
    }

    private fun setOwnerOptionsButtonListener() = with(binding) {
        toolbarContainer.btnMoreOptions.setOnClickListener { optionsButton ->
            val editButton   = optionsMenuBalloonForOwner.getContentView().findViewById<ConstraintLayout>(R.id.btn_edit)
            val removeButton = optionsMenuBalloonForOwner.getContentView().findViewById<ConstraintLayout>(R.id.btn_remove)

            editButton.setOnClickListener {
                optionsMenuBalloonForOwner.dismiss()
                partyEditContract.launch(viewModel.party.value)
            }

            removeButton.setOnClickListener {
                optionsMenuBalloonForOwner.dismiss()
                showCustomDialog(
                    title          = "??????",
                    message        = "?????? ?????? ????????? ?????????????????????????",
                    positiveMethod = { viewModel.occurEvent(PartyAction.OnDeletePartyMenuClicked) },
                    negativeMethod = null
                )
            }

            optionsMenuBalloonForOwner.showAlignBottom(optionsButton)
        }
    }

    private fun setUserOptionsButtonListener() = with(binding) {
        toolbarContainer.btnMoreOptions.setOnClickListener { optionsButton ->
            val leaveButton = optionsMenuBalloonForUser.getContentView().findViewById<ConstraintLayout>(R.id.btn_leave)

            leaveButton.setOnClickListener {
                optionsMenuBalloonForUser.dismiss()
                showCustomDialog(
                    title          = "??????",
                    message        = "?????? ?????? ????????? ??????????????????????",
                    positiveMethod = { viewModel.occurEvent(PartyAction.OnLeavePartyMenuClicked) },
                    negativeMethod = null
                )
            }

            optionsMenuBalloonForUser.showAlignBottom(optionsButton)
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
                0 -> tab.text = "??????"
                1 -> tab.text = "????????????"
            }
        }.attach()
    }

    private fun openOpenKakaoTalk(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

}
