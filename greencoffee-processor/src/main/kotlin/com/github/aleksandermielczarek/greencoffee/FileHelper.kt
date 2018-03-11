package com.github.aleksandermielczarek.greencoffee

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic
import javax.tools.StandardLocation


/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
class FileHelper(private val processingEnv: ProcessingEnvironment) {

    fun getAndroidTestPath(): Path {
        val dummyFile = processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "tmp")
        Thread.sleep(500)
        val dummyPath = Paths.get(dummyFile.toUri())
        val appName = processingEnv.options[GreenCoffeeProcessor.OPTIONS_APP_FOLDER] ?: "app"
        val appPath = dummyPath.find { Files.isDirectory(it) && it.endsWith(appName) } ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find app directory for appFolder: $appName")
            throw IllegalStateException()
        }
        dummyFile.delete()
        val appIndex = dummyPath.indexOf(appPath)
        return dummyPath.root.resolve(dummyPath.subpath(0, appIndex + 1)).resolve(Paths.get("src", "androidTest"))
    }
}