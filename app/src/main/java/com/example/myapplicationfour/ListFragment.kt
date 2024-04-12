package com.example.myapplicationfour

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ListFragment : Fragment() {

    // Creates a new fragment given parameters
    // ListFragment.newInstance()
    companion object {

        private const val ITEM_LIST = "ItemList"

        fun newInstance(itemList: String?): ListFragment {
            val fragmentList = ListFragment()
            val args = Bundle()
            args.putString(ITEM_LIST, itemList)
            fragmentList.arguments = args
            return fragmentList
        }
    }

    // Define the events that the fragment will use to communicate
    interface OnItemEditListener{
        // This can be any number of events to be sent to the activity
        fun onEditItem(edit: Bundle)
    }

    // Define the listener of the interface type
    // listener will the activity instance containing fragment
    private var listenerEdit: OnItemEditListener? = null

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    var itemsList: MutableList<Item> = mutableListOf<Item>()

    private var argsItemsList: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listenerEdit = context as ListFragment.OnItemEditListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        argsItemsList = getArguments()?.getString(ITEM_LIST, "Edit")

        stringToList(argsItemsList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializationRVAdapter(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        listenerEdit  = null
    }

    private fun stringToList(jsonString: String?):MutableList<Item> {

        this.itemsList.addAll( jsonString?.let {
            Json.decodeFromString(ListSerializer(Item.serializer()),
                it)
        } as MutableList<Item>)

        return itemsList
    }

    //порядок функций для адаптера
    private fun initializationRVAdapter(view: View) {
        this.recyclerView = view.findViewById(R.id.rvList)
        // Assign itemList to ItemAdapter
        this.itemAdapter = ItemAdapter(itemsList){ item -> onItemClicked(item)  }
        // Set the LayoutManager that this RecyclerView will use.
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        // adapter instance is set to the recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter
        //когда recyclerView не планирует изменять размеры своих дочерних элементов динамически
        recyclerView.setHasFixedSize(true)
    }

    // Now we can fire the event when the user selects something in the fragment
    private fun onEditClicked(bundle: Bundle){
        listenerEdit?.onEditItem(bundle)
    }

    private fun onItemClicked(item: Item){

        val args = Bundle()
        args.putString("Action", "Edit")
        args.putInt("Id1", item.id!!)
        args.putInt("Id2", item.id!!)
        args.putString("Title", item.title)
        args.putString("Description", item.description)
        args.putString("Priority", item.priority!!.toString())
        args.putString("Type", item.type)
        args.putString("Count", item.count!!.toString())
        args.putString("Period", item.period!!.toString())

        onEditClicked(args)
    }

}
