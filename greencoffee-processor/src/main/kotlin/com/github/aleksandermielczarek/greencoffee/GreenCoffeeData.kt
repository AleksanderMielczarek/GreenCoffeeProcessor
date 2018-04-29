package com.github.aleksandermielczarek.greencoffee

import java.util.*

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
data class GreenCoffeeData(
        val screenshotOnFail: Boolean,
        val featureFromAssets: String,
        val tags: List<String>,
        val locales: List<Locale>,
        val includeScenarios: List<String>,
        val excludeScenarios: List<String>
) {
    companion object {

        fun fromGreenCoffee(greenCoffee: GreenCoffee): GreenCoffeeData {
            return GreenCoffeeData(
                    greenCoffee.screenshotOnFail,
                    greenCoffee.featureFromAssets,
                    greenCoffee.tags.toList(),
                    greenCoffee.locales.map { Locale(it.language, it.country, it.variant) },
                    greenCoffee.includeScenarios.toList(),
                    greenCoffee.excludeScenarios.toList()
            )
        }
    }
}