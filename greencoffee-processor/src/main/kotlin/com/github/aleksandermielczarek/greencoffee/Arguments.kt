package com.github.aleksandermielczarek.greencoffee

import javax.annotation.processing.ProcessingEnvironment

class Arguments(processingEnv: ProcessingEnvironment) {

    companion object {
        const val OPTIONS_APP_FOLDER = "appFolder"
    }

    val appFolder = processingEnv.options[OPTIONS_APP_FOLDER] ?: "app"
}