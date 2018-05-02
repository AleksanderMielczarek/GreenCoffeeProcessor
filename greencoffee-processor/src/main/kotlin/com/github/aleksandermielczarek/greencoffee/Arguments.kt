package com.github.aleksandermielczarek.greencoffee

import javax.annotation.processing.ProcessingEnvironment

class Arguments(private val processingEnv: ProcessingEnvironment) {

    companion object {
        const val OPTIONS_APP_FOLDER = "appFolder"
        const val OPTIONS_SUPPORTED_ANNOTATIONS = "supportedAnnotations"
    }

    fun appFolder(): String {
        return processingEnv.options[OPTIONS_APP_FOLDER] ?: "app"
    }

    fun supportedAnnotations(): List<String> {
        return processingEnv.options[OPTIONS_SUPPORTED_ANNOTATIONS]?.split(",")?.map { it.trim() }
                ?: emptyList()
    }
}