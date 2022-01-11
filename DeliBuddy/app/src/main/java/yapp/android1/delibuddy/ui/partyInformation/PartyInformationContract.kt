package yapp.android1.delibuddy.ui.partyInformation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Action.PartyAction

const val EDIT_PARTYINFO  = "partyInformation"

internal class PartyInformationContract : ActivityResultContract<PartyInformation, PartyInformationViewModel.Action>() {
    override fun createIntent(context: Context, input: PartyInformation?): Intent {
        val intent = Intent(context, CreatePartyActivity::class.java)
        intent.putExtra(EDIT_PARTYINFO, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PartyInformationViewModel.Action {
        return when(resultCode) {
            Activity.RESULT_OK -> PartyAction.PartyEditSuccess
            else               -> PartyAction.PartyEditFailed
        }
    }
}