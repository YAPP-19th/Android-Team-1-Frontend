package yapp.android1.delibuddy.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.IncludeLayoutPartiesEmptyViewBinding
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity

@AndroidEntryPoint
class PartiesEmptyFragment : BaseFragment<IncludeLayoutPartiesEmptyViewBinding>(
    IncludeLayoutPartiesEmptyViewBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.emptyButtonAddButton.setOnClickListener {
            val intent = Intent(activity, CreatePartyActivity::class.java)
            startActivity(intent)
        }
    }

}
