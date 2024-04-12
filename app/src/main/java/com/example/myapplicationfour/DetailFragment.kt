package com.example.myapplicationfour

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    // Creates a new fragment given parameters
    // DetailFragment.newInstance()
    companion object {

        private const val ACTION = "Action"
        private const val ID_1 = "Id1"
        private const val ID_2 = "Id2"
        private const val TITLE = "Title"
        private const val DESCRIPTION = "Description"
        private const val PRIORITY = "Priority"
        private const val TYPE = "TYPE"
        private const val COUNT = "Count"
        private const val PERIOD = "Period"

        fun newInstance(
            action: String?, id1: Int, id2: Int,
            title: String?, description: String?,
            priority: String?, type: String?,
            count: String?, period: String?,
        ): DetailFragment {
            val fragmentDetail = DetailFragment()
            val args = Bundle()

            args.putString(ACTION, action)
            args.putInt(ID_1, id1)
            args.putInt(ID_2, id2)
            args.putString(TITLE, title)
            args.putString(DESCRIPTION, description)
            args.putString(PRIORITY, priority)
            args.putString(TYPE, type)
            args.putString(COUNT, count)
            args.putString(PERIOD, period)

            fragmentDetail.arguments = args
            return fragmentDetail
        }
    }

    // Define the events that the fragment will use to communicate
    interface OnItemCreateUpdateListener{
        // This can be any number of events to be sent to the activity
        fun onCreateItem(create: Bundle)

        fun onUpdateItem(update: Bundle)
    }

    // Define the listener of the interface type
    // listener will the activity instance containing fragment
    private var listenerCreateUpdate: OnItemCreateUpdateListener? = null

    private lateinit var tvAction: TextView
    private lateinit var etTitleDetail: EditText
    private lateinit var etDescriptionDetail: EditText
    private lateinit var spinnerPriorityDetail: Spinner
    private lateinit var rgTypeDetail: RadioGroup
    private lateinit var rbtnType1Detail: RadioButton
    private lateinit var rbtnType2Detail: RadioButton
    private lateinit var etCountDetail: EditText
    private lateinit var etPeriodDetail: EditText
    private lateinit var btnSaveDetail: Button
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>
    var select: String = ""

    private var argAction: String? = null
    private var argId1: Int? = null
    private var argId2: Int? = null
    private var argTitle: String? = null
    private var argDescription: String? = null
    private var argPriority: Int? = null
    private var argType: String? = null
    private var argCount: Int? = null
    private var argPeriod: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listenerCreateUpdate = context as OnItemCreateUpdateListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        argAction = getArguments()?.getString(ACTION, "")
        argId1 = getArguments()?.getInt(ID_1, 0)
        argId2 = getArguments()?.getInt(ID_2, 0)
        argTitle = getArguments()?.getString(TITLE, "")
        argDescription = getArguments()?.getString(DESCRIPTION, "")
        argPriority = getArguments()?.getString(PRIORITY, "0")?.toInt()
        argType = getArguments()?.getString(TYPE, "")
        argCount = getArguments()?.getString(COUNT, "0")?.toInt()
        argPeriod = getArguments()?.getString(PERIOD, "0")?.toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializationViews(view)
        initializationSpinnerAdapter()
        inputValues()
    }

    override fun onDetach() {
        super.onDetach()
        listenerCreateUpdate = null
    }

    // Now we can fire the event when the user selects something in the fragment
    private fun onCreateClicked(bundle: Bundle){
        listenerCreateUpdate?.onCreateItem(bundle)
    }

    // Now we can fire the event when the user selects something in the fragment
    private fun onUpdateClicked(bundle: Bundle) {
        listenerCreateUpdate?.onUpdateItem(bundle)
    }

    private fun initializationViews(view: View) {

        tvAction = view.findViewById(R.id.tvAction)
        etTitleDetail = view.findViewById(R.id.etTitle)
        etDescriptionDetail = view.findViewById(R.id.etDescription)
        spinnerPriorityDetail = view.findViewById(R.id.spinnerPriority)
        rgTypeDetail = view.findViewById(R.id.rgType)
        rbtnType1Detail = view.findViewById(R.id.rbtnTypeOne)
        rbtnType2Detail = view.findViewById(R.id.rbtnTypeTwo)
        etCountDetail = view.findViewById(R.id.etCount)
        etPeriodDetail = view.findViewById(R.id.etPeriod)
        btnSaveDetail = view.findViewById(R.id.btnSave)

        btnSaveDetail.setOnClickListener(){

            val args = Bundle()
            // Passing the data to the MainActivity
            args.putString("Action", tvAction.text.toString())
            args.putInt("Id1", argId1!!)
            args.putInt("Id2", argId2!!)
            args.putString("Title", etTitleDetail.text.toString())
            args.putString("Description", etDescriptionDetail.text.toString())
            args.putString("Priority", spinnerPriorityDetail.selectedItem.toString())
            args.putString("Type", select)
            args.putString("Count", etCountDetail.text.toString())
            args.putString("Period", etPeriodDetail.text.toString())

            if (tvAction.text.toString() == "Create"){
                onCreateClicked(args)
            } else {
                onUpdateClicked(args)
            }
        }

        // check which button was pressed
        rgTypeDetail.setOnCheckedChangeListener { group, checkedId ->
            // on below line we are getting radio button from our group.
            val checkedRbtn = group.findViewById<RadioButton>(checkedId) as RadioButton
            this.select = checkedRbtn.text.toString()
        }
    }

    private fun initializationSpinnerAdapter(){
        // Настраиваем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        arrayAdapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.priorities,
            android.R.layout.simple_spinner_item)
        // Определяем разметку для использования при выборе элемента
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Применяем адаптер к элементу spinner
        spinnerPriorityDetail.adapter = arrayAdapter
    }

    private fun inputValues() {

        tvAction.text = argAction
        etTitleDetail.setText(argTitle)
        etDescriptionDetail.setText(argDescription)
        spinnerPriorityDetail.setSelection(arrayAdapter.getPosition(argPriority.toString()))

        when (argType) {
            "Type 1" -> {
                // option one was clicked
                rbtnType1Detail.setChecked(true)
            }
            "Type 2" -> {
                // option two was clicked
                rbtnType2Detail.setChecked(true)
            }
        }

        etCountDetail.setText(argCount.toString())
        etPeriodDetail.setText(argPeriod.toString())
    }

}
