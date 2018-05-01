package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.given
import com.squareup.kotlinpoet.ClassName
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.lang.model.element.TypeElement

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
@RunWith(MockitoJUnitRunner.StrictStubs::class)
class ProgrammerTest {

    @Mock
    lateinit var typeHelper: TypeHelper

    @Mock
    lateinit var test: TypeElement

    @InjectMocks
    lateinit var programmer: Programmer

    @Before
    fun setUp() {
        given(typeHelper.getPackage(test)).willReturn(TestFactory.testPackage)
        given(typeHelper.getName(test)).willReturn(TestFactory.testName)
        given(typeHelper.getTypeName(test)).willReturn(ClassName(TestFactory.testPackage, TestFactory.testName))
    }

    @Test
    fun `should write complete test`() {
        val code = programmer.writeCode(test, TestFactory.completeGreenCoffeeData, TestFactory.completeScenarios)

        assertEquals(TestFactory.testComplete, code.toString().trimIndent())
    }


    @Test
    fun `should write minimal test`() {
        val code = programmer.writeCode(test, TestFactory.minimalGreenCoffeeData, TestFactory.minimalScenarios)

        assertEquals(TestFactory.testMinimal, code.toString().trimIndent())
    }

    @Test
    fun `should write single elements test`() {
        val code = programmer.writeCode(test, TestFactory.singleElementsGreenCoffeeData, TestFactory.singleElementsScenarios)

        assertEquals(TestFactory.testWithSingleElements, code.toString().trimIndent())
    }
}