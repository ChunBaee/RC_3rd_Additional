package com.jcorp.exams.directmsg.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter (fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    var mFragList = mutableListOf<Fragment>()

    fun setItem(fragList : MutableList<Fragment>) {
        mFragList = fragList
    }

    override fun getItemCount(): Int {
        return mFragList.size
    }

    override fun createFragment(position: Int): Fragment {

        return mFragList[position]
    }
}