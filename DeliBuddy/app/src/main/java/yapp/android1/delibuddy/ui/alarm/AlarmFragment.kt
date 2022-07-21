package yapp.android1.delibuddy.ui.alarm

import android.os.Bundle
import android.view.View
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAlarmBinding
import yapp.android1.delibuddy.ui.alarm.adapter.AlarmAdapter
import yapp.android1.delibuddy.ui.myparty.adapter.MyPartyAdapter
import yapp.android1.delibuddy.ui.myparty.adapter.MyPartyMockGenerator

class AlarmFragment : BaseFragment<FragmentAlarmBinding>(
    FragmentAlarmBinding::inflate
) {
    private val alarmAdapter by lazy {
        AlarmAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
//        binding.recyclerView.adapter = alarmAdapter
    }
}
