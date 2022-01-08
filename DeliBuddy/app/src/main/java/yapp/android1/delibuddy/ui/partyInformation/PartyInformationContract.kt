package yapp.android1.delibuddy.ui.partyInformation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity

const val EDIT_PARTYINFO  = "partyInformation"
const val EDIT_PARTYINFO_RESULT  = "result_partyInformation"

internal class PartyInformationContract : ActivityResultContract<PartyInformation, PartyInformationViewModel.PartyInformationAction>() {
    override fun createIntent(context: Context, input: PartyInformation?): Intent {
        val intent = Intent(context, CreatePartyActivity::class.java)
        intent.putExtra(EDIT_PARTYINFO, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PartyInformationViewModel.PartyInformationAction {
        return when(resultCode) {
            Activity.RESULT_OK -> {
                //val editedPartyInformation = intent?.getSerializableExtra(EDIT_PARTYINFO_RESULT) as PartyInformation
                PartyInformationViewModel.PartyInformationAction.PartyEditSuccess
            }
            else -> PartyInformationViewModel.PartyInformationAction.PartyEditFailed
        }
    }
}