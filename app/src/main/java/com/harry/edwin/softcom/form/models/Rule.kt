package com.harry.edwin.softcom.form.models

data class Rule (val condition: String ="",
                 val value: String = "",
                 val action: String = "",
                 val otherwise: String ="",
                 val targets: List<String> = listOf("")
                 )