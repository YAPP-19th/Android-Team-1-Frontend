package yapp.android1.delibuddy.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentHomeBinding
import yapp.android1.delibuddy.ui.home.adapter.PartiesAdapter
import yapp.android1.delibuddy.ui.partyDetail.PartyDetailActivity
import yapp.android1.delibuddy.util.intentTo
import yapp.android1.domain.entity.PartyEntity

const val PARTY_ID = "party id"

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val partiesAdapter: PartiesAdapter by lazy {
        PartiesAdapter {
            adapterOnClick(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPartiesRecyclerView()
        setPartiesAdapterData()

    }

    private fun initPartiesRecyclerView() = with(binding.rvParties) {
        adapter = partiesAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setPartiesAdapterData() {
        partiesAdapter.submitList(
            listOf(
                PartyEntity(1),
                PartyEntity(2),
                PartyEntity(3),
                PartyEntity(4),
                PartyEntity(5),
                PartyEntity(6)
            )
        )
    }

    private fun adapterOnClick(partyEntity: PartyEntity) {
        val bundle = Bundle()
        bundle.putInt(PARTY_ID, partyEntity.id)

        requireActivity().intentTo(PartyDetailActivity::class.java, bundle)
    }
}