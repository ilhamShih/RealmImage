package com.realm.imade.ui.tabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.realm.imade.ui.fragment.FragmentBase
import com.realm.imade.ui.fragment.FragmentFavorite

class TabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentBase()
            else -> {
                FragmentFavorite()
            }
        }
    }
    override fun getItemCount(): Int = 2

}