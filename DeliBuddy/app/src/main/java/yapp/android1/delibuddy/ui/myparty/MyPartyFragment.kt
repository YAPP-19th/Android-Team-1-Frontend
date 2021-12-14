package yapp.android1.delibuddy.ui.myparty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentMypartyBinding
import yapp.android1.delibuddy.ui.myparty.adapter.MyPartyAdapter
import yapp.android1.delibuddy.ui.myparty.adapter.MyPartyMockGenerator

class MyPartyFragment : BaseFragment<FragmentMypartyBinding>(
    FragmentMypartyBinding::inflate
) {
    private val myPartyAdapter by lazy {
        MyPartyAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.recyclerView.adapter = myPartyAdapter
        myPartyAdapter.submitList(MyPartyMockGenerator.getMyPartyList())
    }
}
