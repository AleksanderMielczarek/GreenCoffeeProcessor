package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.net.URI
import java.nio.file.Paths
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.JavaFileObject

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
@RunWith(MockitoJUnitRunner.StrictStubs::class)
class FileHelperTest {

    @Mock
    lateinit var processingEnv: ProcessingEnvironment

    @Mock
    lateinit var dummyFile: JavaFileObject

    @Mock
    lateinit var filer: Filer

    @InjectMocks
    lateinit var helper: FileHelper

    @Before
    fun setUp() {
        given(processingEnv.filer).willReturn(filer)
        given(filer.createSourceFile(any())).willReturn(dummyFile)
        given(dummyFile.toUri()).willReturn(URI.create("file:///C:/Projects/GreenCoffee/app/build/kapt/androidTest/dummy.java"))
    }

    @Test
    fun `should get android test path`() {
        val path = helper.getAndroidTestPath()

        Assert.assertEquals(Paths.get(URI.create("file:///C:/Projects/GreenCoffee/app/src/androidTest")), path)
    }
}