package com.github.aleksandermielczarek.greencoffee

import com.google.common.base.Stopwatch
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.FileObject
import javax.tools.StandardLocation

class AndroidTestPath(
        private val processingEnv: ProcessingEnvironment,
        private val arguments: Arguments,
        private val errors: Errors,
        private val fakeFileTimeout: Int = DEFAULT_FAKE_FILE_TIMEOUT,
        private val fakeFileWait: Long = DEFAULT_FAKE_FILE_WAIT
) {

    companion object {
        const val DEFAULT_FAKE_FILE_TIMEOUT = 5000
        const val DEFAULT_FAKE_FILE_WAIT = 500L
    }

    fun getAndroidTestPath(): Path {
        val dummyFile = processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "tmp")

        waitUntilFileIsCreated(dummyFile)

        val dummyPath = Paths.get(dummyFile.toUri())
        val appPath = dummyPath.find { it.startsWith(arguments.appFolder()) && it.endsWith(arguments.appFolder()) }
                ?: run { appNotFound(dummyFile) }
        val appIndex = dummyPath.indexOf(appPath)

        dummyFile.delete()

        return dummyPath.root.resolve(dummyPath.subpath(0, appIndex + 1)).resolve(Paths.get("src", "androidTest"))
    }

    private fun waitUntilFileIsCreated(dummyFile: FileObject) {
        val stopwatch = Stopwatch.createStarted()
        while (true) {
            try {
                dummyFile.toUri()
                stopwatch.stop()
                break
            } catch (t: Throwable) {
                if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > fakeFileTimeout) {
                    appNotFound(dummyFile)
                }
                Thread.sleep(fakeFileWait)
            }
        }
    }

    private fun appNotFound(dummyFile: FileObject) {
        dummyFile.delete()
        errors.appDirectoryNotFound(arguments.appFolder())
    }
}