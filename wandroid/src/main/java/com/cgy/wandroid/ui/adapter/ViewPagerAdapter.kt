package com.cgy.wandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author: cgy
 * @description:
 * @date: 2019/10/30 10:40
 */
class ViewPagerAdapter(fm : FragmentManager, private val fragments : List<SupportFragment>) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}