package yapp.android1.delibuddy.ui.createparty

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.databinding.ActivityCreatePartyBinding
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.ui.partyInformation.EDIT_PARTYINFO
import yapp.android1.delibuddy.util.extensions.gone
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.extensions.show
import yapp.android1.domain.entity.PartyCreationRequestEntity
import yapp.android1.domain.entity.PartyEditRequestEntity

@AndroidEntryPoint
class CreatePartyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePartyBinding
    private val viewModel: CreatePartyViewModel by viewModels()

    private val targetTime = Calendar.getInstance()
    private val targetTimes = arrayOf<Int>(0, 0, 0, 0, 0)
    private val selectedTimes = arrayOf<Int>(0, 0, 0, 0, 0)
    private var dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    private var selectedMember = 0
    private var selectedCategoryId = 0

    private val MAX_TITLE = 10
    private val MAX_BODY = 255

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddressActivity.ADDRESS_ACTIVITY_RESULT_CODE) {
            val data: Intent = result.data!!
            val selectedAddress =
                data.getParcelableExtra<Address>(AddressActivity.ADDRESS_ACTIVITY_USER_ADDRESS)
            viewModel.occurEvent(CreatePartyEvent.SelectedAddress(selectedAddress))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePartyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTime()
        initView()
        initObserve()
    }

    private fun onIntent() {
        val intentData = intent.getSerializableExtra(EDIT_PARTYINFO) as PartyInformation

    }

    override fun onResume() {
        super.onResume()
        binding.spinnerCategory.setSelection(viewModel.currentCategoryIndex.value, false)
    }

    private fun initTime() {
        targetTime.add(Calendar.MINUTE, 10)
        targetTimes[PartyTimeElement.YEAR.ordinal] = targetTime.get(Calendar.YEAR)
        targetTimes[PartyTimeElement.MONTH.ordinal] = targetTime.get(Calendar.MONTH)
        targetTimes[PartyTimeElement.DAY.ordinal] = targetTime.get(Calendar.DAY_OF_MONTH)
        targetTimes[PartyTimeElement.HOUR.ordinal] = targetTime.get(Calendar.HOUR_OF_DAY)
        targetTimes[PartyTimeElement.MINUTE.ordinal] = targetTime.get(Calendar.MINUTE)

        for (i in selectedTimes.indices) {
            selectedTimes[i] = targetTimes[i]
        }
        selectedTimes[PartyTimeElement.MONTH.ordinal] += 1
    }

    private fun initView() = with(binding) {
        tvCreateParty.setOnClickListener {
            createParty()
        }

        tvEditParty.setOnClickListener {
            editParty()
        }

        tvPartyDate.text =
            "${targetTimes[PartyTimeElement.MONTH.ordinal] + 1}월 ${targetTimes[PartyTimeElement.DAY.ordinal]}일"
        tvPartyTime.text =
            "${targetTimes[PartyTimeElement.HOUR.ordinal]}시 ${targetTimes[PartyTimeElement.MINUTE.ordinal]}분"

        ivReset.setOnClickListener {
            viewModel.occurEvent(CreatePartyEvent.ClearAddress)
        }

        tvPartyAddress.setOnClickListener {
            val intent = Intent(this@CreatePartyActivity, AddressActivity::class.java)
            getResult.launch(intent)
        }

        ivIconExit.setOnClickListener {
            finish()
        }

        initTitleTextWatcher()
        initChatTextWatcher()
        initBodyTextWatcher()
        initDatePicker()
        initTimePicker()
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
                                CreatePartyEvent.ChangeFlags(PartyElement.TITLE, false)
                            )
                            tvPartyTitleError.visibility = View.VISIBLE
                        }
                        title.length > MAX_TITLE -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlags(PartyElement.TITLE, false)
                            )
                            tvPartyTitleError.visibility = View.VISIBLE
                        }
                        else -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlags(PartyElement.TITLE, true)
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
                                CreatePartyEvent.ChangeFlags(PartyElement.BODY, false)
                            )
                            tvPartyBodyError.visibility = View.VISIBLE
                        }
                        title.length > MAX_BODY -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlags(PartyElement.BODY, false)
                            )
                            tvPartyBodyError.visibility = View.VISIBLE
                        }
                        else -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlags(PartyElement.BODY, true)
                            )
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
                                CreatePartyEvent.ChangeFlags(PartyElement.CHAT_URL, false)
                            )
                            tvChatUrlError.visibility = View.VISIBLE
                        }
                        !isMatchWithRule -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlags(PartyElement.CHAT_URL, false)
                            )
                            tvChatUrlError.visibility = View.VISIBLE
                        }
                        else -> {
                            viewModel.occurEvent(
                                CreatePartyEvent.ChangeFlags(PartyElement.CHAT_URL, true)
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
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                selectedTimes[PartyTimeElement.YEAR.ordinal] = year
                selectedTimes[PartyTimeElement.MONTH.ordinal] = month + 1
                selectedTimes[PartyTimeElement.DAY.ordinal] = dayOfMonth
                validateTime()
                tvPartyDate.text = "${month + 1}월 ${dayOfMonth}일"
            }

        val datePickerDialog =
            DatePickerDialog(
                this@CreatePartyActivity,
                datePickerDialogListener,
                targetTimes[PartyTimeElement.YEAR.ordinal],
                targetTimes[PartyTimeElement.MONTH.ordinal],
                targetTimes[PartyTimeElement.DAY.ordinal]
            )

        tvPartyDate.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun initTimePicker() = with(binding) {
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            selectedTimes[PartyTimeElement.HOUR.ordinal] = hourOfDay
            selectedTimes[PartyTimeElement.MINUTE.ordinal] = minute
            validateTime()
            tvPartyTime.text = "${hourOfDay}시 ${minute}분"
        }

        val timePickerDialog = TimePickerDialog(
            this@CreatePartyActivity,
            timePickerListener,
            targetTimes[PartyTimeElement.HOUR.ordinal],
            targetTimes[PartyTimeElement.MINUTE.ordinal],
            false
        )

        tvPartyTime.setOnClickListener {
            timePickerDialog.show()
        }
    }

    private fun validateTime() = with(binding) {
        if (isSelectedDatePast()) {
            viewModel.occurEvent(CreatePartyEvent.ChangeFlags(PartyElement.TIME, false))
            tvPartyTimeError.visibility = View.VISIBLE
        } else {
            viewModel.occurEvent(CreatePartyEvent.ChangeFlags(PartyElement.TIME, true))
            tvPartyTimeError.visibility = View.GONE
        }
    }

    private fun isSelectedDatePast(): Boolean {
        Timber.w("selectedMonth: ${selectedTimes[PartyTimeElement.MONTH.ordinal]}")
        val selectedDate = dateFormat.parse(
            "%04d".format(selectedTimes[PartyTimeElement.YEAR.ordinal]) + "-" +
                    "%02d".format(selectedTimes[PartyTimeElement.MONTH.ordinal]) + "-" +
                    "%02d".format(selectedTimes[PartyTimeElement.DAY.ordinal]) + " " +
                    "%02d".format(selectedTimes[PartyTimeElement.HOUR.ordinal]) + ":" +
                    "%02d".format(selectedTimes[PartyTimeElement.MINUTE.ordinal]) + ":59"
        )
        return targetTime.time.time - selectedDate.time > 0
    }

    private fun initCategorySpinnerAfterGetListFromServer() = with(binding) {
        // val categories = arrayOf("음식 카테고리", "한식", "일식", "양식", "중식")
        val categories = listOf<String>("음식 카테고리") + viewModel.categoryList.value.map { it.name }
        val categorySpinnerAdapter = ArrayAdapter(
            this@CreatePartyActivity,
            android.R.layout.simple_spinner_dropdown_item,
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
                            CreatePartyEvent.ChangeFlags(PartyElement.CATEGORY, false)
                        )
                        tvPartyCategoryError.visibility = View.VISIBLE
                    }
                    else -> {
                        viewModel.occurEvent(
                            CreatePartyEvent.ChangeFlags(PartyElement.CATEGORY, true)
                        )
                        tvPartyCategoryError.visibility = View.GONE
                        selectedCategoryId = viewModel.categoryList.value[position - 1].id
                        viewModel.occurEvent(CreatePartyEvent.SelectedCategory(position))
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
            android.R.layout.simple_spinner_dropdown_item,
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
                        viewModel.occurEvent(CreatePartyEvent.ChangeFlags(PartyElement.MEMBER, false))
                        tvPartyMemberError.visibility = View.VISIBLE
                    }
                    else -> {
                        viewModel.occurEvent(CreatePartyEvent.ChangeFlags(PartyElement.MEMBER, true))
                        tvPartyMemberError.visibility = View.GONE
                        selectedMember = position + 1
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun createParty() = with(binding) {
        val partyAddress = viewModel.currentAddress.value!!
        val coordString = "POINT (${partyAddress.lng} ${partyAddress.lat})"
        val orderTimeString =
            "20" + "%02d".format(selectedTimes[PartyTimeElement.MONTH.ordinal]) + "-" +
                    "%02d".format(selectedTimes[PartyTimeElement.MONTH.ordinal]) + "-" +
                    "%02d".format(selectedTimes[PartyTimeElement.DAY.ordinal]) + " " +
                    "%02d".format(selectedTimes[PartyTimeElement.HOUR.ordinal]) + ":" +
                    "%02d".format(selectedTimes[PartyTimeElement.MINUTE.ordinal]) + ":00"
        val newParty = PartyCreationRequestEntity(
            body = etPartyBody.text.toString(),
            categoryId = selectedCategoryId,
            coordinate = coordString,
            openKakaoUrl = etChatUrl.text.toString(),
            orderTime = orderTimeString,
            placeName = partyAddress.addressName,
            placeNameDetail = partyAddress.addressDetail,
            targetUserCount = selectedMember,
            title = etPartyTitle.text.toString()
        )
        viewModel.occurEvent(CreatePartyEvent.CreatePartyClick(newParty))
    }

    private fun editParty() = with(binding) {
        val partyAddress = viewModel.currentAddress.value!!
        val coordString = "POINT (${partyAddress.lng} ${partyAddress.lat})"

        val changedPartyInfo = PartyEditRequestEntity(
            body = etPartyBody.text.toString(),
            coordinate = coordString,
            title = etPartyTitle.text.toString()
        )

        viewModel.occurEvent(CreatePartyEvent.EditParty(changedPartyInfo))
    }

    private fun initObserve() = with(binding) {
        repeatOnStarted {
            viewModel.currentAddress.collect { address ->
                val isAvailable = when {
                    address == null -> false
                    address.addressName == "주소를 입력해 주세요" -> false
                    else -> true
                }

                if (isAvailable) {
                    viewModel.occurEvent(
                        CreatePartyEvent.ChangeFlags(PartyElement.ADDRESS, true)
                    )
                    tvPartyAddress.text = address!!.addressName
                    tvPartyAddress.typeface = Typeface.DEFAULT_BOLD
                    tvPartyAddressError.visibility = View.GONE
                    ivReset.visibility = View.VISIBLE
                } else {
                    viewModel.occurEvent(
                        CreatePartyEvent.ChangeFlags(PartyElement.ADDRESS, false)
                    )
                    tvPartyAddress.text = "위치 추가"
                    tvPartyAddress.typeface = Typeface.DEFAULT
                    tvPartyAddressError.visibility = View.VISIBLE
                    ivReset.visibility = View.GONE
                }
            }
        }


        repeatOnStarted {
            viewModel.canCreateParty.collect {
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
            viewModel.canEditParty.collect { canEdit ->
                if(canEdit) {
                    tvEditParty.setTextColor(
                        ContextCompat.getColor(
                            this@CreatePartyActivity,
                            yapp.android1.delibuddy.R.color.main_orange
                        )
                    )
                    tvEditParty.typeface = Typeface.DEFAULT_BOLD
                } else {
                    tvEditParty.setTextColor(
                        ContextCompat.getColor(
                            this@CreatePartyActivity,
                            yapp.android1.delibuddy.R.color.text_grey
                        )
                    )
                    tvEditParty.typeface = Typeface.DEFAULT
                }
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect {
                Toast.makeText(this@CreatePartyActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        repeatOnStarted {
            viewModel.invalidElement.collect { flag ->
                when (flag) {
                    PartyElement.TITLE -> tvPartyTitleError.visibility = View.VISIBLE
                    PartyElement.CATEGORY -> tvPartyCategoryError.visibility = View.VISIBLE
                    PartyElement.TIME -> tvPartyTimeError.visibility = View.VISIBLE
                    PartyElement.MEMBER -> tvPartyMemberError.visibility = View.VISIBLE
                    PartyElement.CHAT_URL -> tvChatUrlError.visibility = View.VISIBLE
                    PartyElement.ADDRESS -> tvPartyAddressError.visibility = View.VISIBLE
                    PartyElement.BODY -> tvPartyBodyError.visibility = View.VISIBLE
                    PartyElement.NONE -> {
                    }
                }
            }
        }

        repeatOnStarted {
            viewModel.categoryList.collect {
                initCategorySpinnerAfterGetListFromServer()
            }
        }

        repeatOnStarted {
            viewModel.isSuccessToCreateParty.collect { isSuccessToCreateParty ->
                if (isSuccessToCreateParty) finish()
            }
        }

        repeatOnStarted {
            viewModel.editingPartyInformation.collect { partyInformation ->
                if(partyInformation != null) {
                    spinnerCategory.gone()
                    spinnerCategoryDropdown.gone()
                    spinnerMember.gone()
                    spinnerMemberDropdown.gone()
                    tvPartyDate.gone()
                    tvPartyTime.gone()
                    tvPartyTimeDummy.gone()

                    etChatUrl.gone()
                    tvEditParty.show()
                    tvCreatePartyTitle.text = "파티 수정하기"
                    tvCreateParty.gone()

                    spinnerCategory.onItemSelectedListener = null

                    etPartyTitle.setText(partyInformation.title)
                    etPartyBody.setText(partyInformation.body)
                }
            }
        }

        repeatOnStarted {
            viewModel.isSuccessToEditParty.collect { isSuccess ->
                if(isSuccess) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@CreatePartyActivity, "수정에 실패했습니다 잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

