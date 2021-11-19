package yapp.android1.delibuddy.ui.location.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.adapter.LocationSearchAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentLocationSearchBinding
import yapp.android1.delibuddy.holder.OnItemClickListener
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class LocationSearchFragment :
    BaseFragment<FragmentLocationSearchBinding>(FragmentLocationSearchBinding::inflate) {
    private val viewModel: LocationSearchViewModel by viewModels()
//    private val actViewModel: LocationViewModel by activityViewModels()
    private lateinit var addressAdapter: LocationSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //initView()
        //initListener()
        initRecyclerView()
        initObserve()

        binding.etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.occurEvent(LocationSearchEvent.SearchAddress(binding.etSearchKeyword.text.toString()))
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initObserve() {
        repeatOnStarted {
            viewModel.addressList.collect {
                addressAdapter.addResult(it)
            }
        }
        repeatOnStarted {
            viewModel.searchQuery.collect {
                addressAdapter.searchQuery = it
                addressAdapter.dataChanged()
            }
        }
    }

    private fun initRecyclerView() {
        addressAdapter = LocationSearchAdapter().apply {
            listener = object : OnItemClickListener {
                override fun onItemClick(position: Int) {
//                    addressAdapter.getItem(position).lat
//                    actViewModel.occure
                }
            }
        }

        binding.rvSearchResult.setHasFixedSize(true)
        binding.rvSearchResult.adapter = addressAdapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(activity)
    }
}