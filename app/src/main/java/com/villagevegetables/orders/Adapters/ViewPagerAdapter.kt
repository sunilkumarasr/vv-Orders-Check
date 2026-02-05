package com.villagevegetables.orders.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.villagevegetables.orders.Fragments.MyOrders.PendingOrdersFragment
import com.villagevegetables.orders.Fragments.MyOrders.CompleteOrdersFragment
import com.villagevegetables.orders.Fragments.MyOrders.NewOrdersFragment
import com.villagevegetables.orders.Fragments.MyOrders.PickUpFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4 // Number of fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewOrdersFragment()
            1 -> PendingOrdersFragment()
            2 -> PickUpFragment()
            3 -> CompleteOrdersFragment()
            else -> NewOrdersFragment()
        }
    }
}