package com.example.myapplicationfour

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    // Creates a new fragment given parameters
    // ListFragment.newInstance()
    companion object {

        private const val ITEM_LIST_1 = "ItemList1"
        private const val ITEM_LIST_2 = "ItemList2"
        private const val ID_1 = "Id1"
        private const val ID_2 = "Id2"

        fun newInstance(itemList1: String?, itemList2: String?, id1: Int, id2: Int): MainFragment {
            val fragmentMain = MainFragment()
            val args = Bundle()
            args.putString(ITEM_LIST_1, itemList1)
            args.putString(ITEM_LIST_2, itemList2)
            args.putInt(ID_1, id1)
            args.putInt(ID_2, id2)
            fragmentMain.arguments = args
            return fragmentMain
        }
    }

    // Define the events that the fragment will use to communicate
    interface OnItemAddListener{
        // This can be any number of events to be sent to the activity
        fun onAddItem(add: Bundle)
    }

    // Define the listener of the interface type
    // listener will the activity instance containing fragment
    private var listenerAdd: OnItemAddListener? = null

    private var argsItemsList1: String? = null
    private var argsItemsList2: String? = null
    private var argId1: Int? = null
    private var argId2: Int? = null

    // When requested, this adapter returns a ObjectFragment,
    // representing an object in the collection.
    private lateinit var pageAdapter: FragmentListStateAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var addFab: FloatingActionButton


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerAdd = context as MainFragment.OnItemAddListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        argsItemsList1 = getArguments()?.getString(MainFragment.ITEM_LIST_1, "")
        argId1 = getArguments()?.getInt(ID_1, 0)
        argsItemsList2 = getArguments()?.getString(MainFragment.ITEM_LIST_2, "")
        argId2 = getArguments()?.getInt(ID_2, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializationFAB(view)
        initializationViewPager(view)
    }

    override fun onDetach() {
        super.onDetach()
        listenerAdd = null
    }

    private fun initializationViewPager(view: View){
        viewPager = view.findViewById(R.id.view_pager2)
        pageAdapter = FragmentListStateAdapter(this, transferBundle())
        viewPager.adapter = pageAdapter

        tabLayout = view.findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = "Type ${(position + 1)}"
            }
        ).attach()
    }


    private fun transferBundle():Bundle {
        val args = Bundle()
        args.putString(ITEM_LIST_1, argsItemsList1)
        args.putString(ITEM_LIST_2, argsItemsList2)
        return args
    }


    private fun initializationFAB(view: View){
        // Register the FABs with ID
        addFab = view.findViewById(R.id.fab)
        // do something when the button is clicked
        addFab.setOnClickListener {
        // Passing the data to the DetailFragment
            val args = Bundle()
            args.putString("Action", "Create")
            args.putInt("Id1", argId1!!)
            args.putInt("Id2", argId2!!)
            onAddClicked(args)
        }
    }

    // Now we can fire the event when the user selects something in the fragment
    fun onAddClicked(bundle: Bundle){
        listenerAdd?.onAddItem(bundle)
    }

}
