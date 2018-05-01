package com.github.aleksandermielczarek.greencoffee

import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class Errors(private val processingEnv: ProcessingEnvironment) {

    fun appDirectoryNotFound(appFolder: String): Nothing {
        throwError("Can't find app directory for appFolder: $appFolder.")
    }

    fun kaptDirectoryNotFound(): Nothing {
        throwError("Can't find the target directory for generated Kotlin files.")
    }

    private fun throwError(message: String): Nothing {
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
        error(message)
    }
}