package com.harry.edwin.softcom.form.models

data class Element(
    val isMandatory: Boolean,
    val file: String = "",
    val label: String,
    val rules: List<Any>,
    val type: String,
    val unique_id: String
)