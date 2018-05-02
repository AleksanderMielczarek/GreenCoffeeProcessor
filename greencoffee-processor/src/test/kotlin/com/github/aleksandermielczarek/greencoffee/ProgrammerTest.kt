package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.squareup.kotlinpoet.ClassName
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.lang.model.element.TypeElement

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class ProgrammerTest {

    @Mock
    lateinit var typeHelper: TypeHelper

    @Mock
    lateinit var arguments: Arguments

    @Mock
    lateinit var test: TypeElement

    @InjectMocks
    lateinit var programmer: Programmer

    val completeScenarios = TestFactory.completeScenarioNames.map { scenario ->
        mock<ReflectiveScenarioConfig> {
            on { camelCaseName() } doReturn scenario
            on { annotations(any()) } doReturn TestFactory.completeScenarioAnnotations
        }
    }
    val minimalScenarios = TestFactory.minimalScenarioNames.map { scenario ->
        mock<ReflectiveScenarioConfig> {
            on { camelCaseName() } doReturn scenario
            on { annotations(any()) } doReturn TestFactory.minimalScenarioAnnotations
        }
    }
    val singleElementsScenarios = TestFactory.singleElementsScenarioNames.map { scenario ->
        mock<ReflectiveScenarioConfig> {
            on { camelCaseName() } doReturn scenario
            on { annotations(any()) } doReturn TestFactory.singleElementsScenarioAnnotations
        }
    }

    @Before
    fun setUp() {
        given(typeHelper.getPackage(test)).willReturn(TestFactory.testPackage)
        given(typeHelper.getName(test)).willReturn(TestFactory.testName)
        given(typeHelper.getTypeName(test)).willReturn(ClassName(TestFactory.testPackage, TestFactory.testName))
    }

    @Test
    fun `write complete test`() {
        val code = programmer.writeCode(test, TestFactory.completeGreenCoffeeData, completeScenarios)

        assertEquals(TestFactory.testComplete, code.toString().trimIndent())
    }

    @Test
    fun `write minimal test`() {
        val code = programmer.writeCode(test, TestFactory.minimalGreenCoffeeData, minimalScenarios)

        assertEquals(TestFactory.testMinimal, code.toString().trimIndent())
    }

    @Test
    fun `write single elements test`() {
        val code = programmer.writeCode(test, TestFactory.singleElementsGreenCoffeeData, singleElementsScenarios)

        assertEquals(TestFactory.testWithSingleElements, code.toString().trimIndent())
    }
}