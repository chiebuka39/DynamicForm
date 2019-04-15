package com.harry.edwin.softcom.di

import com.harry.edwin.softcom.form.viewmodel.FormViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    viewModel { FormViewModel() }
}