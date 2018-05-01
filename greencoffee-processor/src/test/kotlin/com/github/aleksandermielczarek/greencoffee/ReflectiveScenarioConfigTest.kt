package com.github.aleksandermielczarek.greencoffee

import com.mauriciotogneri.greencoffee.Scenario
import com.mauriciotogneri.greencoffee.ScenarioConfig
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.squareup.kotlinpoet.ClassName
import org.junit.Assert.assertEquals
import org.junit.Test

class ReflectiveScenarioConfigTest {

    val scenario = Scenario(
            "Invalid username and password.!@ #$%^&*()-=+`~,./<>?[] {};'\\:\"|",
            listOf(
                    "@Ignore",
                    "@FlakyTest",
                    "@SmallTest",
                    "@MediumTest",
                    "@LargeTest",
                    "@org.junit.Ignore",
                    "@android.support.test.filters.FlakyTest",
                    "@android.support.test.filters.SmallTest",
                    "@android.support.test.filters.MediumTest",
                    "@android.support.test.filters.LargeTest",
                    "@com.foo.Bar",
                    "@Foo"
            ))
    val scenarioConfig: ScenarioConfig = mock {
        on { scenario() } doReturn scenario
    }

    val reflectiveScenarioConfig = ReflectiveScenarioConfig(scenarioConfig)

    @Test
    fun `format camel case name`() {
        assertEquals("InvalidUsernameAndPassword", reflectiveScenarioConfig.camelCaseName())
    }

    @Test
    fun `get annotations`() {
        val expectedAnnotations = listOf(
                ClassName("org.junit", "Ignore"),
                ClassName("android.support.test.filters", "FlakyTest"),
                ClassName("android.support.test.filters", "SmallTest"),
                ClassName("android.support.test.filters", "MediumTest"),
                ClassName("android.support.test.filters", "LargeTest"),
                ClassName("org.junit", "Ignore"),
                ClassName("android.support.test.filters", "FlakyTest"),
                ClassName("android.support.test.filters", "SmallTest"),
                ClassName("android.support.test.filters", "MediumTest"),
                ClassName("android.support.test.filters", "LargeTest"),
                ClassName("com.foo", "Bar")
        )

        assertEquals(expectedAnnotations, reflectiveScenarioConfig.annotations())
    }
}