package yapp.android1.delibuddy.ui.partyInformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ActivityPartyInformationBinding

@AndroidEntryPoint
class PartyInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartyInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_information)
    }
}