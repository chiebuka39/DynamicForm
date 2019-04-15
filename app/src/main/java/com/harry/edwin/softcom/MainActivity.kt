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
import com.harry.edwin.softcom.form.models.Element
import com.harry.edwin.softcom.form.models.Rule
import com.harry.edwin.softcom.form.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class MainActivity : AppCompatActivity(), SelectDateFragment.OnDateReceiveCallBack {
    override fun onDateReceive(yy: Int, mm: Int, dd: Int) {

        val id = myModel.selectedDate.value?.first


        try {
            myModel.selectedDate.value = Pair(id!!, "$yy-$mm-$dd")
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

    val ids_ by lazy {   mutableMapOf<Int, Element>() }
    val answers by lazy {   mutableMapOf<Int, String>() }
    val rules by lazy {   mutableMapOf<Int, Int>() }
    var idsList_ =  mutableListOf<Pair<Int,Element>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val gson = Gson();

        val form = gson.fromJson(readJSONFromAsset("pet_adoption-1"), FormData::class.java)

        myModel.setFormDataLiveData(form)


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
                                            ids_[textId.removeAt(0)] = element
                                        }else if(element.type.equals("formattednumeric")){
                                            ids_[numbersId.removeAt(0)] = element
                                        }else if(element.type.equals("datetime")){
                                            ids_[datesId.removeAt(0)] = element
                                        }else if(element.type.equals("yesno")){
                                            ids_[yesNoId.removeAt(0)] = element
                                        }
                            }
                    }

        }

        ids_.forEach { entry ->
            answers.put(entry.key, "")
            if (entry.value.rules.isNotEmpty()){


                rule = gson.fromJson(entry.value.rules.first().toString(), Rule::class.java)
                ruleId = entry.key
//                Log.v("Ebuka", rule.targets.toString())
//                rules.put(entry.key, rule)
            }else{
                if (entry.value.unique_id.equals(rule.targets.first())){
                    rules.put(ruleId, entry.key)
                }
                rule = Rule()
            }

        }

        myModel.rulesLiveData.value = rules
        myModel.idLiveData.value = ids_
        myModel.answersLiveData.value = answers
        idsList_.addAll(ids_.toList())


        val adapter = FormPagerAdapter(supportFragmentManager, tabs.getTabCount())
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

        tabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        if (tabs.tabCount == 2) {
            tabs.tabMode = TabLayout.MODE_FIXED
        } else {
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }


    }

    var rule : Rule = Rule()
    var ruleId  = 0

    fun getId() : Pair<Int, Element>{
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
