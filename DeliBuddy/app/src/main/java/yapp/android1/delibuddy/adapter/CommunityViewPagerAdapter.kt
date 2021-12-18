package yapp.android1.delibuddy.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import yapp.android1.delibuddy.ui.partyInformation.fragments.CommentTabFragment
import yapp.android1.delibuddy.ui.partyInformation.fragments.UserListTabFragment


class CommunityViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return COMMUNITY_TAB_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CommentTabFragment()
            1 -> UserListTabFragment()
            else -> throw IllegalArgumentException("올바른 Position이 아닙니다.")
        }
    }

    companion object {
        const val COMMUNITY_TAB_COUNT = 2
    }

}