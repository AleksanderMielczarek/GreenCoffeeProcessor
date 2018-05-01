package com.github.aleksandermielczarek.greencoffee

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GreenCoffee(
        val screenshotOnFail: Boolean = false,
        val featureFromAssets: String = "",
        val tags: Array<String> = [],
        val locales: Array<ScenarioLocale> = [],
        val includeScenarios: Array<String> = [],
        val excludeScenarios: Array<String> = []
)