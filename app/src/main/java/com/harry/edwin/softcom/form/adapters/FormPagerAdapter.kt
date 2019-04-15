package com.harry.edwin.softcom.form.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.harry.edwin.softcom.form.FormFragent

class FormPagerAdapter(val fm: FragmentManager,val numOfTabs: Int) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return FormFragent.newInstance(position, numOfTabs);
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "harry"
    }

    override fun getCount(): Int {
        return numOfTabs;
    }
}