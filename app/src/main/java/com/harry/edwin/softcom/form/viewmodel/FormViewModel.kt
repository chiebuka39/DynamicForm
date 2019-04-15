package com.harry.edwin.softcom.form.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.harry.edwin.softcom.form.models.Element
import com.harry.edwin.softcom.form.models.FormData

class FormViewModel : ViewModel() {
    private val formDataLiveData : MutableLiveData<FormData> = MutableLiveData()
    val idLiveData : MutableLiveData<Map<Int, Element>> = MutableLiveData()
    val answersLiveData : MutableLiveData<MutableMap<Int, String>> = MutableLiveData()
    val selectedDate : MutableLiveData<Pair<Int, String>> = MutableLiveData()

    fun setFormDataLiveData(formData: FormData){
        formDataLiveData.value = formData
    }

    fun getFormData() : FormData? {
        //answersLiveData.value = answersLiveData.value?.put()
        return formDataLiveData.value
    }


}
