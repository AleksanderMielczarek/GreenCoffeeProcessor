package com.github.aleksandermielczarek.greencoffee

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
class FileHelper(private val processingEnv: ProcessingEnvironment) {

    fun getAndroidTestPath(): Path {
        val dummyFile = processingEnv.filer.createSourceFile("GreenCoffeeProcessor_${System.currentTimeMillis()}")
        val processorPath = Paths.get(dummyFile.toUri())
        val appName = processingEnv.options[GreenCoffeeProcessor.OPTIONS_APP_FOLDER] ?: "app"
        val testPath = processorPath.find { Files.isDirectory(it) && it.endsWith(appName) } ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find app directory for appFolder: $appName")
            throw IllegalStateException()
        }
        dummyFile.delete()
        return testPath.resolve(Paths.get("src", "androidTest"))
    }
}