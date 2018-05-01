package com.github.aleksandermielczarek.greencoffee

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

class Scenarios {

    fun getScenarios(greenCoffee: GreenCoffeeData, androidTestPath: Path): List<ReflectiveScenarioConfig> {
        val feature = Files.newInputStream(androidTestPath.resolve(greenCoffee.featureFromAssets))
        val scenarios = getScenarios(greenCoffee, feature)
        feature.use { }
        return scenarios
    }

    private fun getScenarios(greenCoffee: GreenCoffeeData, feature: InputStream): List<ReflectiveScenarioConfig> {
        return ReflectiveGreenCoffeeConfig(greenCoffee.screenshotOnFail)
                .withFeatureFromInputStream(feature)
                .withTags(greenCoffee.tags)
                .scenarios(greenCoffee.locales)
                .filter {
                    if (greenCoffee.includeScenarios.isNotEmpty()) {
                        greenCoffee.includeScenarios.contains(it.scenario().name())
                    } else {
                        true
                    }
                }
                .filter {
                    if (greenCoffee.excludeScenarios.isNotEmpty()) {
                        !greenCoffee.excludeScenarios.contains(it.scenario().name())
                    } else {
                        true
                    }
                }
    }
}

