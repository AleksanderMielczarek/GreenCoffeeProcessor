package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI
import java.nio.file.Paths
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.FileObject

class AndroidTestPathTest {

    val fakePath = "file:/home/user/StudioProjects/android-dazn-app/app/build/kapt"
    val expectedPath = Paths.get(URI.create("file:/home/user/StudioProjects/android-dazn-app/app/src/androidTest"))

    val dummyFile: FileObject = mock {
        on { toUri() } doReturn URI.create(fakePath)
    }
    val filer: Filer = mock {
        on { createResource(any(), any(), any()) } doReturn dummyFile
    }
    val messager: Messager = mock()
    val processingEnv: ProcessingEnvironment = mock {
        on { filer } doReturn filer
        on { messager } doReturn messager
    }
    val arguments: Arguments = mock {
        on { appFolder() } doReturn "app"
    }
    val errors: Errors = spy(Errors(processingEnv))

    val androidTestPath = AndroidTestPath(processingEnv, arguments, errors, 500, 100)

    @Test
    fun `find android test path`() {
        val androidTestPath = androidTestPath.getAndroidTestPath()

        assertEquals(expectedPath, androidTestPath)
    }

    @Test
    fun `fail and then find android test path`() {
        given(dummyFile.toUri())
                .willThrow(RuntimeException())
                .willReturn(URI.create(fakePath))

        val androidTestPath = androidTestPath.getAndroidTestPath()

        assertEquals(expectedPath, androidTestPath)
        then(dummyFile).should(times(3)).toUri()
    }

    @Test(expected = IllegalStateException::class)
    fun `throw error if fake file is not found within given timeout`() {
        given(dummyFile.toUri())
                .willAnswer {
                    Thread.sleep(100)
                    throw IllegalStateException()
                }

        androidTestPath.getAndroidTestPath()

        then(errors).should().appDirectoryNotFound("app")
    }
}