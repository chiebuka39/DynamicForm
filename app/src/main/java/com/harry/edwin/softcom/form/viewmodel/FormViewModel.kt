package com.harry.edwin.softcom.form.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.harry.edwin.softcom.form.models.FormData

class FormViewModel : ViewModel() {
    private val formDataLiveData : MutableLiveData<FormData> = MutableLiveData()
    val idLiveData : MutableLiveData<Map<String, Int>> = MutableLiveData()
    val selectedDate : MutableLiveData<Pair<Int, String>> = MutableLiveData()

    fun setFormDataLiveData(formData: FormData){
        formDataLiveData.value = formData
    }

    fun getFormData() : FormData? {
        return formDataLiveData.value
    }


}
