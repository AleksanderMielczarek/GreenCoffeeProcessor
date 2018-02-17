package com.github.aleksandermielczarek.greencoffee

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
class FileHelper(private val processingEnv: ProcessingEnvironment) {

    fun getFeatureFile(greenCoffee: GreenCoffeeData): InputStream {
        val coffeePath = Paths.get(processingEnv.filer.createSourceFile("GreenCoffeeProcessor_${System.currentTimeMillis()}").toUri())
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "Dummy: $coffeePath")
        val appName = processingEnv.options[GreenCoffeeProcessor.OPTIONS_APP_FOLDER] ?: "app"
        val testPath = coffeePath.find { Files.isDirectory(it) && it.endsWith(appName) } ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find app directory for appFolder: $appName")
            throw IllegalStateException()
        }
        return Files.newInputStream(testPath.resolve(Paths.get("src", "androidTest", greenCoffee.featureFromAssets)))
    }
}