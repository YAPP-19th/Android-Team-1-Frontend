package yapp.android1.delibuddy.ui.partyInformation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.FragmentStatusBottomSheetDialogBinding
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent.*
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus


internal class StatusBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentStatusBottomSheetDialogBinding? = null
    val binding get() = _binding!!

    private val viewModel by activityViewModels<PartyInformationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() = with(binding) {
        tvItem1.setOnClickListener {
            viewModel.occurEvent(OnStatusChanged(PartyStatus.YET_ORDER))
            dismiss()
        }
        tvItem2.setOnClickListener {
            viewModel.occurEvent(OnStatusChanged(PartyStatus.ORDER))
            dismiss()
        }
        tvItem3.setOnClickListener {
            viewModel.occurEvent(OnStatusChanged(PartyStatus.COMPLETED))
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}