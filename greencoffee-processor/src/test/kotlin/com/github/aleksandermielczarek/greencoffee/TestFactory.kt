package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.mock
import java.util.*

object TestFactory {

    val testPackage = "com.test"
    val testName = "Test"

    val completeScenarios = listOf<ScenarioConfig>(mock(), mock())
    val minimalScenarios = listOf<ScenarioConfig>(mock())
    val singleElementsScenarios = listOf<ScenarioConfig>(mock())

    val completeGreenCoffeeData = GreenCoffeeData(
            true,
            "assets/login.feature",
            listOf("tag1", "tag2"),
            listOf(Locale("pl", "PL"), Locale("en", "GB")),
            listOf("include1", "include2"),
            listOf("exclude1", "exclude2")
    )
    val minimalGreenCoffeeData = GreenCoffeeData(
            false,
            "assets/login.feature",
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
    )
    val singleElementsGreenCoffeeData = GreenCoffeeData(
            true,
            "assets/login.feature",
            listOf("tag1"),
            listOf(Locale("pl", "PL")),
            listOf("include1"),
            listOf("exclude1")
    )

    val testComplete = """
            package com.test

            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import java.util.Locale

            class GreenCoffeeTest1 : Test(GreenCoffeeConfig(true)
                .withFeatureFromAssets("assets/login.feature")
                .withTags("tag1", "tag2")
                .scenarios(Locale("pl", "PL", ""), Locale("en", "GB", ""))
                .filter { listOf("include1", "include2").contains(it.scenario().name()) }
                .filter { !listOf("exclude1", "exclude2").contains(it.scenario().name()) }
                [0]
            )

            class GreenCoffeeTest2 : Test(GreenCoffeeConfig(true)
                .withFeatureFromAssets("assets/login.feature")
                .withTags("tag1", "tag2")
                .scenarios(Locale("pl", "PL", ""), Locale("en", "GB", ""))
                .filter { listOf("include1", "include2").contains(it.scenario().name()) }
                .filter { !listOf("exclude1", "exclude2").contains(it.scenario().name()) }
                [1]
            )
        """.trimIndent()

    val testMinimal = """
            package com.test

            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig

            class GreenCoffeeTest1 : Test(GreenCoffeeConfig(false)
                .withFeatureFromAssets("assets/login.feature")
                .scenarios()
                [0]
            )
        """.trimIndent()

    val testWithSingleElements = """
            package com.test

            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import java.util.Locale

            class GreenCoffeeTest1 : Test(GreenCoffeeConfig(true)
                .withFeatureFromAssets("assets/login.feature")
                .withTags("tag1")
                .scenarios(Locale("pl", "PL", ""))
                .filter { listOf("include1").contains(it.scenario().name()) }
                .filter { !listOf("exclude1").contains(it.scenario().name()) }
                [0]
            )
        """.trimIndent()
}