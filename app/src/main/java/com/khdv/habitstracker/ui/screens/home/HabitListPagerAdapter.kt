package com.khdv.habitstracker.ui.screens.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khdv.habitstracker.model.Habit

class HabitListPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when(position) {
        0 -> HabitListFragment.newInstance(Habit.Type.USEFUL)
        else -> HabitListFragment.newInstance(Habit.Type.HARMFUL)
    }
}