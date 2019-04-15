package com.harry.edwin.softcom.extras

import android.widget.DatePicker
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.harry.edwin.softcom.MainActivity
import com.harry.edwin.softcom.interfaces.DateSetListener
import java.util.*






class SelectDateFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var mListener: SelectDateFragment.OnDateReceiveCallBack? = null
    private var context_: Context? = null

    interface OnDateReceiveCallBack {
        fun onDateReceive(yy: Int, mm: Int, dd: Int)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.context_ = context

        try {
            mListener = context as SelectDateFragment.OnDateReceiveCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnDateSetListener")
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(context, this, yy, mm, dd)
    }



    override fun onDateSet(view: DatePicker, yy: Int, mm: Int, dd: Int) {
        populateSetDate(yy, mm + 1, dd)
    }

    fun populateSetDate(year: Int, month: Int, day: Int) {
        mListener?.onDateReceive(year,month,day)
    }

}