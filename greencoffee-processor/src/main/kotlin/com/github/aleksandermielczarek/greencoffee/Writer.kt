package com.github.aleksandermielczarek.greencoffee

import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class Writer(private val processingEnv: ProcessingEnvironment) {

    fun writeFile(code: FileSpec) {
        val kaptGeneratedDirPath = processingEnv.options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")
                ?: run {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
                    throw IllegalStateException()
                }
        val kaptGeneratedDir = File(kaptGeneratedDirPath)
        if (!kaptGeneratedDir.parentFile.exists()) {
            kaptGeneratedDir.parentFile.mkdirs()
        }
        code.writeTo(kaptGeneratedDir)
    }
}