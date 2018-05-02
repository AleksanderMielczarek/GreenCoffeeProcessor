package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.annotation.processing.ProcessingEnvironment

class ArgumentsTest {

    val processingEnv = mock<ProcessingEnvironment>()
    val arguments = Arguments(processingEnv)

    @Test
    fun `return default app if no argument was passed`() {
        assertEquals("app", arguments.appFolder())
    }

    @Test
    fun `return app passed in argument`() {
        given(processingEnv.options).willReturn(mapOf("appFolder" to "customApp"))

        assertEquals("customApp", arguments.appFolder())
    }

    @Test
    fun `return empty annotations if no argument was passed`() {
        assertEquals(emptyList<String>(), arguments.supportedAnnotations())
    }

    @Test
    fun `return annotations passed in argument`() {
        given(processingEnv.options).willReturn(mapOf("supportedAnnotations" to "com.foo.Bar,com.bar.Foo"))

        assertEquals(listOf("com.foo.Bar", "com.bar.Foo"), arguments.supportedAnnotations())
    }

    @Test
    fun `trim annotations passed in argument`() {
        given(processingEnv.options).willReturn(mapOf("supportedAnnotations" to " com.foo.Bar , com.bar.Foo "))

        assertEquals(listOf("com.foo.Bar", "com.bar.Foo"), arguments.supportedAnnotations())
    }
}