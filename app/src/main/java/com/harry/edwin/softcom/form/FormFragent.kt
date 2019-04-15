package com.harry.edwin.softcom.form

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
import com.harry.edwin.softcom.MainActivity
import com.harry.edwin.softcom.extras.SelectDateFragment
import com.harry.edwin.softcom.interfaces.DateSetListener
import java.lang.Exception


val POSITION  = "position"
class FormFragent : Fragment(), DateSetListener {
    override fun populateSetDate(yr: Int, mon: Int, dy: Int) {
        this.year = yr
        this.month = mon
        this.day = dy

    }

    companion object {
        fun newInstance(position:Int) = FormFragent().apply {
            arguments = Bundle().apply {
                putInt(POSITION,position)
            }
        }
    }

    var mPosition = 0
    var mPage: Page? = null
    val sharedViewModel by sharedViewModel<FormViewModel>()

    val calendar by lazy {   Calendar.getInstance() }
    var year = 0

    var month  = 0
    var day  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            mPosition = it?.getInt(POSITION, 0)!!
        }
    }

    val secondIds_ by lazy { sharedViewModel.idLiveData.value?.toList() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.form_fragent_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        mPage = sharedViewModel.getFormData()?.pages?.get(mPosition)

        mPage?.sections?.forEach { section ->
            addTextViews(section.label, 15f)
            addSection(section)
        }

        sharedViewModel.selectedDate.observe(this, androidx.lifecycle.Observer {
                date ->
            val textView = view.findViewById<TextView>(date.first)
            try {
                textView.text = date.second
            }catch (e:Exception){

            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



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

    //This function to convert DPs to pixels
    private fun convertDpToPixel(dp: Float): Int {
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }



    private fun addEditTexts(element: Element) {
        val editTextLayout = LinearLayout(activity)
        editTextLayout.orientation = LinearLayout.VERTICAL

        linearLayout.addView(editTextLayout)


        val editText = EditText(activity)
        editText.hint = element.label

        if (element.type.equals("formattednumeric")){
            editText.inputType = InputType.TYPE_CLASS_NUMBER

            editText.id = (activity as MainActivity).getId().second
        }else{
            editText.inputType = InputType.TYPE_CLASS_TEXT

            editText.id = (activity as MainActivity).getId().second
        }

        setEditTextAttributes(editText)
        editTextLayout.addView(editText)

    }

    private fun setEditTextAttributes(editText: EditText) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            0
        )

        editText.layoutParams = params
    }

    private fun addImageToLayout(element: Element) {




        val imageViewLayout = LinearLayout(activity)
        imageViewLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(imageViewLayout)


        val imageView = ImageView(activity)

        setImageViewAttributes(imageView, element)
        imageViewLayout.addView(imageView)

//        addLineSeperator()
    }

    private fun setImageViewAttributes(imageView: ImageView, element: Element){
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            200
        )

        params.setMargins(
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            0
        )

        imageView.layoutParams = params
        Log.v("harry", element.file)
        Glide.with(this)
            .load(element.file)
            .centerCrop()
            .into(imageView)
    }

    private fun addTextViews(label: String, size: Float = 6f) {
        //Adding a LinearLayout with HORIZONTAL orientation
        val textLinearLayout = LinearLayout(activity)
        textLinearLayout.orientation = LinearLayout.HORIZONTAL

        linearLayout.addView(textLinearLayout)


        val textView = TextView(activity)
        textView.text = label
        textView.textSize = size
        setTextViewAttributes(textView)
        textLinearLayout.addView(textView)

    }

    private fun setTextViewAttributes(textView: TextView) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            0, 0
        )

        textView.setTextColor(Color.BLACK)
        textView.layoutParams = params
    }

    private fun addDatePicker(element: Element) {
        val datePickerLayout = LinearLayout(activity)
        datePickerLayout.orientation = LinearLayout.HORIZONTAL

        linearLayout.addView(datePickerLayout)
        // TODO: pop the id and save it in a bundle
        val pickDate = Button(activity)
        val dateText = TextView(activity)
        pickDate.text = element.label
        dateText.text = ""
        dateText.id =  (activity as MainActivity).getId().second

        pickDate.setOnClickListener {
            val newFragment = SelectDateFragment()
            sharedViewModel.selectedDate.value = Pair(dateText.id,"")
            newFragment.show(fragmentManager!!, "DatePicker")

        }

        setButtonAttributes(pickDate)
        setTextViewAttributes(dateText)
        datePickerLayout.addView(pickDate)
        datePickerLayout.addView(dateText)


    }

    private fun setButtonAttributes(pickDate: Button) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            0, 0
        )

        pickDate.setTextColor(Color.BLACK)
        pickDate.layoutParams = params
    }

    private fun createRadioGroup(element: Element) {

        val radioGroupLayout = LinearLayout(activity)
        radioGroupLayout.orientation = LinearLayout.VERTICAL

        val textView = TextView(activity)
        textView.text = element.label
        textView.textSize = 12f
        setTextViewAttributes(textView)


        val rb = arrayOfNulls<RadioButton>(5)
        val rg = RadioGroup(activity) //create the RadioGroup
                rg.orientation = RadioGroup.HORIZONTAL//or RadioGroup.VERTICAL
        for (i in 0 until 2)
        {
            rb[i] = RadioButton(activity)
            if (i == 0){
                rb[i]?.text = "Yes"
            }else{
                rb[i]?.text = "No"
            }

//            rb[i].setId(i + 100)

            rg.addView(rb[i])

        }
        setRadioGroupAttr(rg)

        rg.id = (activity as MainActivity).getId().second
        radioGroupLayout.addView(textView)
        radioGroupLayout.addView(rg)

        linearLayout.addView(radioGroupLayout)//you add the whole RadioGroup to the layout

    }

    private fun setRadioGroupAttr(radioGroup: RadioGroup) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            convertDpToPixel(16f),
            0
        )

        radioGroup.layoutParams = params
    }



}
