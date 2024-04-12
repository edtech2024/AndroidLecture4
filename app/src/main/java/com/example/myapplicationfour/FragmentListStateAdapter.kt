package com.example.myapplicationfour

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentListStateAdapter(fragment: Fragment, var bundle: Bundle) : FragmentStateAdapter(fragment) {

    // возвращает количество страниц, которые будут в ViewPager2
    override fun getItemCount(): Int = 2

    // по номеру страницы, передаваемому в качестве параметра position, возвращает объект фрагмента
    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> ListFragment.newInstance(bundle.getString("ItemList1"))
            1 -> ListFragment.newInstance(bundle.getString("ItemList2"))
            else ->  throw Exception("Error")
        }

    }

}
