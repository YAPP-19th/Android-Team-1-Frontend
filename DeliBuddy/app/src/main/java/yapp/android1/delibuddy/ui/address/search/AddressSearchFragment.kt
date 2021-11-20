package yapp.android1.delibuddy.ui.address.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.adapter.AddressSearchAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressSearchBinding
import yapp.android1.delibuddy.holder.OnItemClickListener
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class AddressSearchFragment :
    BaseFragment<FragmentAddressSearchBinding>(FragmentAddressSearchBinding::inflate) {
    private val viewModel: AddressSearchViewModel by viewModels()

    //    private val actViewModel: LocationViewModel by activityViewModels()
    private lateinit var addressAdapter: AddressSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initObserve()

        binding.etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.occurEvent(AddressSearchEvent.SearchAddress(binding.etSearchKeyword.text.toString()))
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initObserve() {
        repeatOnStarted {
            viewModel.addressList.collect {
                addressAdapter.updateResult(it)
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
        addressAdapter = AddressSearchAdapter().apply {
            listener = object : OnItemClickListener {
                override fun onItemClick(position: Int) {
//                    actViewModel.occure
                    val address = addressAdapter.getItem(position)
                    Timber.w(address.lat.toString() + ", " + address.lon.toString())
                }
            }
        }

        binding.rvSearchResult.setHasFixedSize(true)
        binding.rvSearchResult.adapter = addressAdapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(activity)
    }
}