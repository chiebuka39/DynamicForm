package com.harry.edwin.softcom.form

import android.content.Intent
import android.graphics.Color
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.harry.edwin.softcom.MainActivity
import com.harry.edwin.softcom.R
import com.harry.edwin.softcom.SuccessActivity
import com.harry.edwin.softcom.extras.SelectDateFragment
import com.harry.edwin.softcom.form.models.Element
import com.harry.edwin.softcom.form.viewmodel.FormViewModel


abstract class CreateFormFragment : Fragment(){

    abstract val sharedViewModel : FormViewModel
    abstract var mLinearLayout : LinearLayout


    fun addSubmitButton(dActivity: Class<SuccessActivity>) {

        val submitButton = Button(activity)
        submitButton.text = getString(R.string.submit)
        val errors = mutableListOf<String>()

        submitButton.setOnClickListener {

            Log.v("Ebuka", sharedViewModel.answersLiveData.value?.toString())

            sharedViewModel.idLiveData.value?.forEach {
                    entry ->
                val answer = sharedViewModel.answersLiveData.value?.get(entry.key)
                if(entry.value.isMandatory){

                    if (answer?.isEmpty()!!){

                        errors.add("${entry.value.label} should not be empty")
                    }
                }
            }

            if (errors.size > 0){

                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Yo have successfully put in all your values", Toast.LENGTH_LONG).show()
                activity?.startActivity(Intent(activity, dActivity))
            }
        }


        setSubmitButtonAttributes(submitButton)
        mLinearLayout.addView(submitButton)
    }



    private fun setSubmitButtonAttributes(submitButton: Button) {
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

        submitButton.layoutParams = params
    }

    //This function to convert DPs to pixels
    private fun convertDpToPixel(dp: Float): Int {
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }


    fun addImageToLayout(element: Element) {

        val imageViewLayout = LinearLayout(activity)
        imageViewLayout.orientation = LinearLayout.VERTICAL
        mLinearLayout.addView(imageViewLayout)


        val imageView = ImageView(activity)

        setImageViewAttributes(imageView, element)
        imageViewLayout.addView(imageView)

//        addLineSeperator()
    }

    private fun setImageViewAttributes(imageView: ImageView, element: Element){
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            400
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

    fun addEditTexts(element: Element) {
        val editTextLayout = LinearLayout(activity)
        editTextLayout.orientation = LinearLayout.VERTICAL

        mLinearLayout.addView(editTextLayout)


        val editText = EditText(activity)
        editText.hint = element.label

        if (element.type.equals("formattednumeric")){
            editText.inputType = InputType.TYPE_CLASS_NUMBER

            editText.id = (activity as MainActivity).getId().first
        }else{
            editText.inputType = InputType.TYPE_CLASS_TEXT

            editText.id = (activity as MainActivity).getId().first
        }

        editText.addTextChangedListener {
                text ->
            sharedViewModel.answersLiveData.value?.set(editText.id, text.toString())
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


    fun addTextViews(label: String, size: Float = 6f) {
        //Adding a LinearLayout with HORIZONTAL orientation
        val textLinearLayout = LinearLayout(activity)
        textLinearLayout.orientation = LinearLayout.HORIZONTAL

        mLinearLayout.addView(textLinearLayout)


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

    fun addDatePicker(element: Element) {
        val datePickerLayout = LinearLayout(activity)
        datePickerLayout.orientation = LinearLayout.HORIZONTAL

        mLinearLayout.addView(datePickerLayout)
        // TODO: pop the id and save it in a bundle
        val pickDate = Button(activity)
        val dateText = TextView(activity)
        pickDate.text = element.label
        dateText.text = ""
        dateText.id =  (activity as MainActivity).getId().first

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

    fun createRadioGroup(element: Element) {

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

        rg.id = (activity as MainActivity).getId().first

        rg.setOnCheckedChangeListener {
                radioGroup, i ->
            val EditTextId = sharedViewModel.rulesLiveData.value?.get(rg.id)

            val checkedRadioButton = radioGroup.findViewById(i) as RadioButton
            val isChecked = checkedRadioButton.isChecked

            if (isChecked)
            {
                Log.v("Ebuka","here $EditTextId ${sharedViewModel.rulesLiveData.value}")
                val isYes = checkedRadioButton.text.equals("Yes")
                if (isYes){

                    EditTextId?.let { mLinearLayout.findViewById<EditText>(it).visibility = View.VISIBLE }
                    sharedViewModel.answersLiveData.value?.set(rg.id, "yes")
                }else{

                    EditTextId?.let { mLinearLayout.findViewById<EditText>(it).visibility = View.INVISIBLE }
                    sharedViewModel.answersLiveData.value?.set(rg.id, "no")
                }

            }else{

            }


        }
        radioGroupLayout.addView(textView)
        radioGroupLayout.addView(rg)

        mLinearLayout.addView(radioGroupLayout)//you add the whole RadioGroup to the layout

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