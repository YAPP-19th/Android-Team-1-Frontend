package yapp.android1.delibuddy.ui.createparty

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import yapp.android1.delibuddy.databinding.ActivityCreatePartyBinding

class CreatePartyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePartyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePartyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initSpinner()
    }

    private fun initView() = with(binding) {

    }

    private fun initSpinner() {
        initCategorySpinner()
        initMemberSpinner()
    }

    private fun initCategorySpinner() = with(binding) {
        val categories = arrayOf("음식 카테고리", "한식", "일식", "양식", "중식")
        val categorySpinnerAdapter = ArrayAdapter(
            this@CreatePartyActivity,
            R.layout.simple_spinner_dropdown_item,
            categories
        )
        spinnerCategory.adapter = categorySpinnerAdapter
    }

    private fun initMemberSpinner() = with(binding) {
        val members = arrayOf("파티 인원 수", "2명", "3명", "4명", "5명")
        val memberSpinnerAdapter = ArrayAdapter(
            this@CreatePartyActivity,
            R.layout.simple_spinner_dropdown_item,
            members
        )
        spinnerMember.adapter = memberSpinnerAdapter
//        spinnerMember.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                when(position) {
//                    0 -> {
//
//                    }
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//        }
    }
}