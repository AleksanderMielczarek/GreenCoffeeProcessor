package com.github.aleksandermielczarek.greencoffee

import com.mauriciotogneri.greencoffee.Scenario
import com.mauriciotogneri.greencoffee.ScenarioConfig
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class ReflectiveScenarioConfigTest {

    val scenario: Scenario = mock {
        on { name() } doReturn "Invalid username and password.!@ #$%^&*()-=+`~,./<>?[] {};'\\:\"|"
    }
    val scenarioConfig: ScenarioConfig = mock {
        on { scenario() } doReturn scenario
    }

    val reflectiveScenarioConfig = ReflectiveScenarioConfig(scenarioConfig)

    @Test
    fun `format camel case name`() {
        assertEquals("InvalidUsernameAndPassword", reflectiveScenarioConfig.camelCaseName())
    }
}