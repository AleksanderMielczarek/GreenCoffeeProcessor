package com.github.aleksandermielczarek.greencoffee

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class ScenarioCounter {

    private val greenCoffeeConfigClass = Class.forName("com.mauriciotogneri.greencoffee.GreenCoffeeConfig")
    private val greenCoffeeConfigConstructor = greenCoffeeConfigClass.getDeclaredConstructor(String::class.java)
    private val withFeatureFromInputStream = greenCoffeeConfigClass.getDeclaredMethod("withFeatureFromInputStream", InputStream::class.java)
    private val scenarios = greenCoffeeConfigClass.getDeclaredMethod("scenarios", Array<Locale>::class.java)

    private val scenarioConfigClass = Class.forName("com.mauriciotogneri.greencoffee.ScenarioConfig")
    private val scenario = scenarioConfigClass.getDeclaredMethod("scenario")

    private val scenarioClass = Class.forName("com.mauriciotogneri.greencoffee.Scenario")
    private val name = scenarioClass.getDeclaredMethod("name")

    fun count(greenCoffee: GreenCoffeeData, androidTestPath: Path): Int {
        val feature = Files.newInputStream(androidTestPath.resolve(greenCoffee.featureFromAssets))
        val scenariosCount = countScenarios(greenCoffee, feature)
        feature.use { }
        return scenariosCount
    }

    private fun countScenarios(greenCoffee: GreenCoffeeData, feature: InputStream): Int {
        return greenCoffeeConfigConstructor.newInstance(greenCoffee.screenshotPath)
                .let { withFeatureFromInputStream(it, feature) }
                .let { scenarios(it, greenCoffee.locales.toTypedArray()) as List<*> }
                .filter {
                    if (greenCoffee.includeScenarios.isNotEmpty()) {
                        val name = scenario(it).let { scenario -> name(scenario) }
                        greenCoffee.includeScenarios.contains(name)
                    } else {
                        true
                    }
                }
                .filter {
                    if (greenCoffee.excludeScenarios.isNotEmpty()) {
                        val name = scenario(it).let { scenario -> name(scenario) }
                        !greenCoffee.excludeScenarios.contains(name)
                    } else {
                        true
                    }
                }
                .count()
    }
}

