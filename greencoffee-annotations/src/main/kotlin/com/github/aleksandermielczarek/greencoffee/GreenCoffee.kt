package com.github.aleksandermielczarek.greencoffee

/**
 * Created by Aleksander Mielczarek on 13.02.2018.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GreenCoffee(
        val screenshotPath: String = "",
        val featureFromAssets: String = "",
        val includeScenarios: Array<String> = [],
        val excludeScenarios: Array<String> = []
)