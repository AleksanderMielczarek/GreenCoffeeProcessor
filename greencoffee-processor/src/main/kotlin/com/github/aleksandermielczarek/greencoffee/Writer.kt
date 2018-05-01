package com.github.aleksandermielczarek.greencoffee

import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.ProcessingEnvironment

class Writer(processingEnv: ProcessingEnvironment, private val errors: Errors) {

    private val kaptGeneratedDirPath = processingEnv.options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")
            ?: run { errors.kaptDirectoryNotFound() }

    fun writeFile(code: FileSpec) {
        val kaptGeneratedDir = File(kaptGeneratedDirPath)
        if (!kaptGeneratedDir.parentFile.exists()) {
            kaptGeneratedDir.parentFile.mkdirs()
        }
        code.writeTo(kaptGeneratedDir)
    }
}