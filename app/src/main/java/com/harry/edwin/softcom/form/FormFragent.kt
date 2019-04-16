package com.harry.edwin.softcom.form

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.harry.edwin.softcom.R
import com.harry.edwin.softcom.form.models.Page
import com.harry.edwin.softcom.form.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.form_fragent_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

import android.graphics.Color
import android.text.InputType
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.harry.edwin.softcom.form.models.Element
import com.harry.edwin.softcom.form.models.Section
import java.util.*
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.widget.addTextChangedListener
import com.harry.edwin.softcom.MainActivity
import com.harry.edwin.softcom.SuccessActivity
import com.harry.edwin.softcom.extras.SelectDateFragment
import com.harry.edwin.softcom.interfaces.DateSetListener
import java.lang.Exception











val POSITION  = "position"
val NUMOFTABS = "num_tabs"
class FormFragent : CreateFormFragment(), DateSetListener {


    override lateinit var mLinearLayout: LinearLayout

    override fun populateSetDate(yr: Int, mon: Int, dy: Int) {
        this.year = yr
        this.month = mon
        this.day = dy

    }

    companion object {
        fun newInstance(position:Int, numOfTabs : Int) = FormFragent().apply {
            arguments = Bundle().apply {
                putInt(POSITION,position)
                putInt(NUMOFTABS, numOfTabs)
            }
        }
    }

    var mPosition = 0
    var mLastPosition = 0
    var mPage: Page? = null
    override val sharedViewModel by sharedViewModel<FormViewModel>()

    val calendar by lazy {   Calendar.getInstance() }
    var year = 0

    var month  = 0
    var day  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            mPosition = it?.getInt(POSITION, 0)!!
            mLastPosition = it.getInt(NUMOFTABS, 0) - 1
        }
    }

//    val secondIds_ by lazy { sharedViewModel.idLiveData.value?.toList() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.form_fragent_fragment, container, false)
    }

    lateinit var mView: View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLinearLayout = linearLayout

        mView = view
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        mPage = sharedViewModel.getFormData()?.pages?.get(mPosition)

        mPage?.sections?.forEach { section ->
            addTextViews(section.label, 15f)
            addSection(section)
        }
        if (mLastPosition == mPosition){
            addSubmitButton(SuccessActivity::class.java)
        }

        sharedViewModel.selectedDate.observe(this, androidx.lifecycle.Observer {
                datePair ->
            val textView = view.findViewById<TextView>(datePair.first)
            try {
                textView.text = datePair.second
                if (datePair.second.isNotEmpty()){
                    sharedViewModel.answersLiveData.value?.set(datePair.first, datePair.second)
                }

            }catch (e:Exception){

            }
        })
        Log.v("Ebuka", sharedViewModel.answersLiveData.value?.toString())

    }




    private fun addSection(section: Section){
        section.elements.forEach {
            element ->
                if (element.type.equals("embeddedphoto")){
                    addImageToLayout(element)
                }else if(element.type.equals("datetime")) {
                    addDatePicker(element)
                }else if(element.type.equals("yesno")){
                    createRadioGroup(element)
                }
                else{
                    addEditTexts(element)
                }

        }
    }




}
