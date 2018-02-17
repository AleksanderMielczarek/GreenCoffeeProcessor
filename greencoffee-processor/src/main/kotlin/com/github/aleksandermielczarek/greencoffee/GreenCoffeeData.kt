package com.github.aleksandermielczarek.greencoffee

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
data class GreenCoffeeData(
        val screenshotPath: String,
        val featureFromAssets: String,
        val includeScenarios: List<String>,
        val excludeScenarios: List<String>
) {
    companion object {

        fun fromGreenCoffee(greenCoffee: GreenCoffee): GreenCoffeeData {
            return GreenCoffeeData(
                    greenCoffee.screenshotPath,
                    greenCoffee.featureFromAssets,
                    greenCoffee.includeScenarios.toList(),
                    greenCoffee.excludeScenarios.toList()
            )
        }
    }
}