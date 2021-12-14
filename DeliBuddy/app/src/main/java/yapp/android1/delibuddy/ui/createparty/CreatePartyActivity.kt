package yapp.android1.delibuddy.ui.createparty

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.databinding.ActivityCreatePartyBinding
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo


class CreatePartyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePartyBinding
    private val viewModel: CreatePartyViewModel by viewModels()

    private val nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    private val nowMinute = Calendar.getInstance().get(Calendar.MINUTE)
    private val nowYear = Calendar.getInstance().get(Calendar.YEAR)
    private val nowMonth = Calendar.getInstance().get(Calendar.MONTH)
    private val nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private val MAX_TITLE = 10
    private val MAX_BODY = 255

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePartyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTime()
        initView()
        initObserve()
    }

    private fun initTime() {

    }

    private fun initView() = with(binding) {
        tvCreateParty.setOnClickListener {
            viewModel.occurEvent(CreatePartyEvent.ClickCreatePartyEvent)
        }

        tvPartyDate.text = "${nowMonth + 1}월 ${nowDay}일"
        tvPartyTime.text = "${nowHour}시 ${nowMinute}분"

        ivReset.setOnClickListener {
            viewModel.occurEvent(CreatePartyEvent.ClearAddressEvent)
        }

        tvPartyAddress.setOnClickListener {
            viewModel.occurEvent(CreatePartyEvent.SearchAddressEvent)
        }

        initTitleTextWatcher()
        initChatTextWatcher()
        initBodyTextWatcher()
        initDatePicker()
        initTimePicker()
        initCategorySpinner()
        initMemberSpinner()
    }

    private fun initTitleTextWatcher() = with(binding) {
        etPartyTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let { title ->
                    when {
                        title.isBlank() -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.TITLE,
                                    false
                                )
                            )
                            tvPartyTitleError.text = "제목을 입력해 주세요"
                            tvPartyTitleError.visibility = View.VISIBLE
                        }
                        title.length > MAX_TITLE -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.TITLE,
                                    false
                                )
                            )
                            tvPartyTitleError.text = "글자수가 너무 많습니다"
                            tvPartyTitleError.visibility = View.VISIBLE
                        }
                        else -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.TITLE,
                                    true
                                )
                            )
                            tvPartyTitleError.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun initChatTextWatcher() = with(binding) {
        etPartyBody.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let { title ->
                    when {
                        title.isBlank() -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.BODY,
                                    false
                                )
                            )
                            tvPartyBodyError.text = "내용을 입력해 주세요"
                            tvPartyBodyError.visibility = View.VISIBLE
                        }
                        title.length > MAX_BODY -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.BODY,
                                    false
                                )
                            )
                            tvPartyBodyError.text = "글자수가 너무 많습니다"
                            tvPartyBodyError.visibility = View.VISIBLE
                        }
                        else -> {
                            viewModel.occurEvent(CreatePartyEvent.ChangeFlagsEvent(Flag.BODY, true))
                            tvPartyBodyError.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun initBodyTextWatcher() = with(binding) {
        etChatUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let { title ->
                    val checkUrlReg = "^https://open.kakao.com/[a-z]/[A-Za-z0-9]+".toRegex()
                    val isMatchWithRule = checkUrlReg.matches(title.toString())
                    when {
                        title.isBlank() -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.CHAT_URL,
                                    false
                                )
                            )
                            tvChatUrlError.text = "오픈 카카오톡 채팅방 링크를 입력해 주세요"
                            tvChatUrlError.visibility = View.VISIBLE
                        }
                        !isMatchWithRule -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.CHAT_URL,
                                    false
                                )
                            )
                            tvChatUrlError.text = "오픈 카카오톡 채팅방 링크 형식에 어긋납니다"
                            tvChatUrlError.visibility = View.VISIBLE
                        }
                        else -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlagsEvent(
                                    Flag.CHAT_URL,
                                    true
                                )
                            )
                            tvChatUrlError.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun initDatePicker() = with(binding) {
        val datePickerDialogListener =
            DatePickerDialog.OnDateSetListener { _, _, month, dayOfMonth ->
                tvPartyDate.text = "${month + 1}월 ${dayOfMonth}일"
            }

        val datePickerDialog =
            DatePickerDialog(
                this@CreatePartyActivity,
                datePickerDialogListener,
                nowYear,
                nowMonth,
                nowDay
            )
        tvPartyDate.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun initTimePicker() = with(binding) {
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            tvPartyTime.text = "${hourOfDay}시 ${minute}분"
        }
        val timePickerDialog = TimePickerDialog(
            this@CreatePartyActivity,
            timePickerListener,
            nowHour,
            nowMinute,
            false
        )

        tvPartyTime.setOnClickListener {
            timePickerDialog.show()
        }
    }

    private fun initCategorySpinner() = with(binding) {
        // TODO: Get Category List From Server
        val categories = arrayOf("음식 카테고리", "한식", "일식", "양식", "중식")
        val categorySpinnerAdapter = ArrayAdapter(
            this@CreatePartyActivity,
            R.layout.simple_spinner_dropdown_item,
            categories
        )
        spinnerCategory.adapter = categorySpinnerAdapter
        spinnerCategory.setSelection(0, false)
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        viewModel.occurEvent(
                            CreatePartyEvent.ChangeFlagsEvent(
                                Flag.CATEGORY,
                                false
                            )
                        )
                        tvPartyCategoryError.visibility = View.VISIBLE
                    }
                    else -> {
                        viewModel.occurEvent(
                            CreatePartyEvent.ChangeFlagsEvent(
                                Flag.CATEGORY,
                                true
                            )
                        )
                        tvPartyCategoryError.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun initMemberSpinner() = with(binding) {
        val members = arrayOf("파티 인원 수", "2명", "3명", "4명", "5명")
        val memberSpinnerAdapter = ArrayAdapter(
            this@CreatePartyActivity,
            R.layout.simple_spinner_dropdown_item,
            members
        )
        spinnerMember.adapter = memberSpinnerAdapter
        spinnerMember.setSelection(0, false)
        spinnerMember.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        viewModel.occurEvent(CreatePartyEvent.ChangeFlagsEvent(Flag.MEMBER, false))
                        tvPartyMemberError.visibility = View.VISIBLE
                    }
                    else -> {
                        viewModel.occurEvent(CreatePartyEvent.ChangeFlagsEvent(Flag.MEMBER, true))
                        tvPartyMemberError.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initObserve() = with(binding) {
        repeatOnStarted {
            viewModel.currentAddress.collect { address ->
                if (address == null) {
                    viewModel.occurEvent(CreatePartyEvent.ChangeFlagsEvent(Flag.ADDRESS, false))
                    tvPartyAddress.text = "위치 추가"
                    tvPartyAddress.typeface = Typeface.DEFAULT
                    tvPartyAddressError.visibility = View.VISIBLE
                } else {
                    viewModel.occurEvent(CreatePartyEvent.ChangeFlagsEvent(Flag.ADDRESS, true))
                    tvPartyAddress.text = address.addressName
                    tvPartyAddress.typeface = Typeface.DEFAULT_BOLD
                    tvPartyAddressError.visibility = View.GONE
                }
            }
        }

        repeatOnStarted {
            viewModel.searchAddressIsClicked.collect { isClicked ->
                if (isClicked) {
                    intentTo(AddressActivity::class.java)
                }
            }
        }

        repeatOnStarted {
            viewModel.canCreateParty.collect() {
                if (it) {
                    tvCreateParty.setTextColor(
                        ContextCompat.getColor(
                            this@CreatePartyActivity,
                            yapp.android1.delibuddy.R.color.main_orange
                        )
                    )
                    tvCreateParty.typeface = Typeface.DEFAULT_BOLD
                } else {
                    tvCreateParty.setTextColor(
                        ContextCompat.getColor(
                            this@CreatePartyActivity,
                            yapp.android1.delibuddy.R.color.text_grey
                        )
                    )
                    tvCreateParty.typeface = Typeface.DEFAULT
                }
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect {
                Toast.makeText(this@CreatePartyActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        repeatOnStarted {
            viewModel.wrong.collect { flag ->
                when(flag) {
                    Flag.TITLE -> tvPartyTitleError.visibility = View.VISIBLE
                    Flag.CATEGORY -> tvPartyCategoryError.visibility = View.VISIBLE
                    Flag.TIME -> tvPartyTimeError.visibility = View.VISIBLE
                    Flag.MEMBER -> tvPartyMemberError.visibility = View.VISIBLE
                    Flag.CHAT_URL -> tvChatUrlError.visibility = View.VISIBLE
                    Flag.ADDRESS -> tvPartyAddressError.visibility = View.VISIBLE
                    Flag.BODY -> tvPartyBodyError.visibility = View.VISIBLE
                    else -> {}
                }
            }
        }
    }
}