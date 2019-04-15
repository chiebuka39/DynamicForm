package com.harry.edwin.softcom

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.harry.edwin.softcom.form.models.FormData

import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import com.google.android.material.tabs.TabLayout
import com.harry.edwin.softcom.extras.SelectDateFragment
import com.harry.edwin.softcom.form.adapters.FormPagerAdapter
import com.harry.edwin.softcom.form.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class MainActivity : AppCompatActivity(), SelectDateFragment.OnDateReceiveCallBack {
    override fun onDateReceive(dd: Int, mm: Int, yy: Int) {

        val id = myModel.selectedDate.value?.first


        try {
            myModel.selectedDate.value = Pair(id!!, "$yy-$mm-$yy")
        }catch (e:Exception){

        }


    }

    val textId = mutableListOf(R.id.text_1,R.id.text_2,
        R.id.text_3, R.id.text_4, R.id.text_5, R.id.text_6, R.id.text_7, R.id.text_8)
    val yesNoId = mutableListOf(R.id.yesno_1,R.id.yesno_2,
        R.id.yesno_3, R.id.yesno_4, R.id.yesno_5)
    val datesId = mutableListOf(R.id.datetime_1,R.id.datetime_2,
        R.id.datetime_3, R.id.datetime_4, R.id.datetime_5)
    val numbersId = mutableListOf(R.id.formattednumeric_1,R.id.formattednumeric_2,
        R.id.formattednumeric_3, R.id.formattednumeric_4, R.id.formattednumeric_5)

    private val myModel by viewModel<FormViewModel>()

    val ids_ by lazy {   mutableMapOf<String, Int>() }
    var idsList_ =  mutableListOf<Pair<String,Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val gson = Gson();

        val form = gson.fromJson(readJSONFromAsset("pet_adoption-1"), FormData::class.java)

        myModel.setFormDataLiveData(form)

        //create a mutable list of pages
        // create a Map of the label and id
        // Create a Map of the label and content
        // In the fragment make observe the live data for changes



        for (k in 0 until form.pages.size) {
            tabs.addTab(tabs.newTab().setText("Form ${k+1}"))
        }

        form.pages.forEach {
                page ->
                    page.sections.forEach {
                        section ->
                            section.elements.forEach {
                                    element ->
                                        if(element.type.equals("text")){
                                            ids_.put(element.unique_id, textId.removeAt(0))
                                        }else if(element.type.equals("formattednumeric")){
                                            ids_.put(element.unique_id, numbersId.removeAt(0))
                                        }else if(element.type.equals("datetime")){
                                            ids_.put(element.unique_id, datesId.removeAt(0))
                                        }else if(element.type.equals("yesno")){
                                            ids_.put(element.unique_id, yesNoId.removeAt(0))
                                        }
                            }
                    }

        }
        myModel.idLiveData.value = ids_
        idsList_.addAll(ids_.toList())


        val adapter = FormPagerAdapter(supportFragmentManager, tabs.getTabCount())
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        if (tabs.tabCount == 2) {
            tabs.tabMode = TabLayout.MODE_FIXED
        } else {
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }


    }

    fun getId() : Pair<String, Int>{
        return idsList_.removeAt(0)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun readJSONFromAsset(name: String): String {
        var json: String = ""
        try {
            val inputStream = assets.open("$name.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
            Log.v("Harry", json)
        } catch (ex: IOException) {
            ex.printStackTrace()
            Log.v("Harry", ex.localizedMessage)
            return json
        }

        return json
    }

}
