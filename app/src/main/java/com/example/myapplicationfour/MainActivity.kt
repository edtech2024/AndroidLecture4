package com.example.myapplicationfour

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity(),
                     MainFragment.OnItemAddListener,
                     ListFragment.OnItemEditListener,
                     DetailFragment.OnItemCreateUpdateListener,
                     NavigationView.OnNavigationItemSelectedListener {

    private val itemsList1Key = "ITEMS_LIST_1"
    private val itemsList2Key = "ITEMS_LIST_2"
    private val countVariables1Key = "COUNT_VARIABLE_1"
    private val countVariables2Key = "COUNT_VARIABLE_2"
    var listFirstFragment: MutableList<Item> = mutableListOf<Item>()
    var listSecondFragment: MutableList<Item> = mutableListOf<Item>()
    var newId1List: Int = listFirstFragment.size
    var newId2List: Int = listSecondFragment.size

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // using toolbar as ActionBar

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        
        switchHamburger()

        if (savedInstanceState == null) {
            // Let's first dynamically add a fragment into a frame container
            val str1: String? = listToString(this.listFirstFragment)
            val str2: String? = listToString(this.listSecondFragment)

            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragmentMain: MainFragment = MainFragment.newInstance(str1, str2, newId1List, newId1List)
            fragmentTransaction.replace(R.id.container, fragmentMain) // контейнер в активити
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            switchHamburger()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (item.getItemId() == R.id.nav_home) {
            replaceMainFragment()
        }

        if (item.getItemId() == R.id.nav_about) {
            replaceAboutAppFragment()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //при нажатии кнопки "Назад", сначала должен быть закрыт NavigationView, а затем приложение.
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun switchHamburger(){
        toggle.isDrawerIndicatorEnabled = true // Чтобы получить значок гамбургера, установим индикатор
    }

    private fun switchBack(){
        toggle.isDrawerIndicatorEnabled = false

        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        toggle.setToolbarNavigationClickListener() {

            onBackPressed()

            switchHamburger()
        }
    }

    private fun replaceMainFragment(){

        val str1: String? = listToString(this.listFirstFragment)
        val str2: String? = listToString(this.listSecondFragment)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentMain: MainFragment = MainFragment.newInstance(str1, str2, newId1List, newId2List)
        fragmentTransaction.replace(R.id.container, fragmentMain) // контейнер в активити
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        switchHamburger()
    }

    private fun replaceAboutAppFragment(){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentAboutApp: AboutAppFragment = AboutAppFragment() // newInstance
        fragmentTransaction.replace(R.id.container, fragmentAboutApp) // контейнер в активити
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        switchHamburger()
    }

    // Now we can define the action to take in the activity when the fragment event fires
    // This is implementing the `OnItemAddListener` interface methods
    override fun onAddItem(add: Bundle){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentDetail: DetailFragment = DetailFragment.newInstance(
            add.getString("Action"),
            add.getInt("Id1"),
            add.getInt("Id2"),
            null,
            null,
            null,
            null,
            null,
            null
        )
        fragmentTransaction.add(R.id.container, fragmentDetail)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        switchBack()
    }

    // Now we can define the action to take in the activity when the fragment event fires
    // This is implementing the `OnItemEditListener` interface methods
    override fun onEditItem(edit: Bundle){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentDetail: DetailFragment = DetailFragment.newInstance(
            edit.getString("Action"),
            edit.getInt("Id1"),
            edit.getInt("Id2"),
            edit.getString("Title"),
            edit.getString("Description"),
            edit.getString("Priority"),
            edit.getString("Type"),
            edit.getString("Count"),
            edit.getString("Period")
        )
        fragmentTransaction.replace(R.id.container, fragmentDetail)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        switchBack()
    }

    // Now we can define the action to take in the activity when the fragment event fires
    // This is implementing the `OnItemCreateUpdateListener` interface methods
    override fun onCreateItem(create: Bundle){

        insertItemInList(bundleToItem(create))
        val str1: String? = listToString(this.listFirstFragment)
        val str2: String? = listToString(this.listSecondFragment)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentMain: MainFragment = MainFragment.newInstance(str1, str2, newId1List, newId2List)
        fragmentTransaction.replace(R.id.container, fragmentMain) // контейнер в активити
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        switchHamburger()

    }

    // Now we can define the action to take in the activity when the fragment event fires
    // This is implementing the `OnItemCreateUpdateListener` interface methods
    override fun onUpdateItem(update: Bundle){

        updateItemInList(bundleToItem(update))
        val str1: String? = listToString(this.listFirstFragment)
        val str2: String? = listToString(this.listSecondFragment)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentMain: MainFragment = MainFragment.newInstance(str1, str2, newId1List, newId2List)
        fragmentTransaction.replace(R.id.container, fragmentMain) // контейнер в активити
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        switchHamburger()
    }

    private fun bundleToItem(bundle: Bundle):Item {
        val item: Item
        if ( bundle.getString("Type") == "Type 1" ){
            item = Item(
                bundle.getInt("Id1"),
                bundle.getString("Title"),
                bundle.getString("Description"),
                (bundle.getString("Priority")?.toInt()),
                bundle.getString("Type"),
                (bundle.getString("Count")?.toInt()),
                (bundle.getString("Period")?.toInt())
            )
        } else {
            item = Item(
                bundle.getInt("Id2"),
                bundle.getString("Title"),
                bundle.getString("Description"),
                (bundle.getString("Priority")?.toInt()),
                bundle.getString("Type"),
                (bundle.getString("Count")?.toInt()),
                (bundle.getString("Period")?.toInt())
            )
        }
        return item
    }

    private fun insertItemInList(item: Item) {
        if (item.type == "Type 1") {
            listFirstFragment.add(item)
            this.newId1List = listFirstFragment.size
        } else {
            listSecondFragment.add(item)
            this.newId2List = listSecondFragment.size
        }
    }

    private fun updateItemInList(item: Item) {
        if (item.type == "Type 1") {
            listFirstFragment[item.id!!] = item
        } else {
            listSecondFragment[item.id!!] = item
        }
    }

    private fun listToString(list: MutableList<Item>):String? {

        val jsonString: String? = Json.encodeToString(ListSerializer(Item.serializer()), list)

        return jsonString

    }

    private fun stringToList(jsonString: String?):MutableList<Item> {

        val itemsList: MutableList<Item> = mutableListOf<Item>()

        itemsList.addAll( jsonString?.let {
            Json.decodeFromString(ListSerializer(Item.serializer()),
                it)
        } as MutableList<Item>)

        return itemsList
    }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {

        outState.putSerializable(countVariables1Key, this.newId1List)
        outState.putSerializable(countVariables2Key, this.newId2List)

        var jsonString1: String? = Json.encodeToString(ListSerializer(Item.serializer()), this.listFirstFragment )
        var jsonString2: String? = Json.encodeToString(ListSerializer(Item.serializer()), this.listSecondFragment )

        outState.putString(itemsList1Key, jsonString1)
        outState.putString(itemsList2Key, jsonString2)

        super.onSaveInstanceState(outState)
    }

    // получение ранее сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        this.newId1List = savedInstanceState.getInt(countVariables1Key)
        this.newId2List = savedInstanceState.getInt(countVariables2Key)

        var jsonString1: String? = savedInstanceState.getString(itemsList1Key)
        var jsonString2: String? = savedInstanceState.getString(itemsList2Key)

        this.listFirstFragment.addAll( jsonString1?.let {
            Json.decodeFromString(ListSerializer(Item.serializer()),
                it)
        } as MutableList<Item>)

        this.listSecondFragment.addAll( jsonString2?.let {
            Json.decodeFromString(ListSerializer(Item.serializer()),
                it)
        } as MutableList<Item>)

    }

}
