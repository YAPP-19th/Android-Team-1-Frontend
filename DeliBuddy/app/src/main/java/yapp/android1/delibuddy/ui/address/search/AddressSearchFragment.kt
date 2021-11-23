package yapp.android1.delibuddy.ui.address.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.adapter.AddressSearchAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressSearchBinding
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class AddressSearchFragment :
    BaseFragment<FragmentAddressSearchBinding>(FragmentAddressSearchBinding::inflate) {
    private val viewModel: AddressSearchViewModel by viewModels()

    //    private val actViewModel: LocationViewModel by activityViewModels()
    private var addressAdapter: AddressSearchAdapter = AddressSearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initObserve()

        binding.etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.occurEvent(
                    AddressSearchEvent.SearchAddress(binding.etSearchKeyword.text.toString())
                )
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initObserve() {
        repeatOnStarted {
            viewModel.searchResult.collect {
                addressAdapter.updateResult(it)
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() = with(binding.rvSearchResult) {
        adapter = addressAdapter
        layoutManager = LinearLayoutManager(activity)
        setHasFixedSize(true)
        addressAdapter.setItemClickListener { address ->
            Timber.w(address.lat.toString() + ", " + address.lon.toString())
        }
    }
}