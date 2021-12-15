package yapp.android1.delibuddy.ui.address.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.AddressSearchAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressSearchBinding
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.ui.address.AddressSharedEvent
import yapp.android1.delibuddy.ui.address.AddressSharedViewModel
import yapp.android1.delibuddy.ui.address.detail.AddressDetailFragment
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class AddressSearchFragment :
    BaseFragment<FragmentAddressSearchBinding>(FragmentAddressSearchBinding::inflate) {
    private val viewModel: AddressSharedViewModel by activityViewModels()

    private var addressAdapter: AddressSearchAdapter = AddressSearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initRecyclerView()
        initObserve()
    }

    private fun initView() = with(binding) {
        etSearchKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    btnCurrentLocation.visibility = View.VISIBLE
                } else {
                    btnCurrentLocation.visibility = View.GONE
                }
            }
        })

        etSearchKeyword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.occurEvent(
                    AddressSharedEvent.SearchAddress(binding.etSearchKeyword.text.toString())
                )
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initRecyclerView() = with(binding.rvSearchResult) {
        adapter = addressAdapter
        layoutManager = LinearLayoutManager(activity)
        setHasFixedSize(true)
        addressAdapter.setItemClickListener { address ->
            Timber.w(address.lat.toString() + ", " + address.lng.toString())
            moveToAddressDetailFragment(address)
        }
    }

    private fun moveToAddressDetailFragment(address: Address) {
        viewModel.occurEvent(AddressSharedEvent.SelectAddress(address))
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<AddressDetailFragment>(R.id.fcv_location)
            addToBackStack("Search")
        }
    }

    private fun initObserve() {
        repeatOnStarted {
            viewModel.searchResults.collect {
                addressAdapter.updateResult(it)
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
