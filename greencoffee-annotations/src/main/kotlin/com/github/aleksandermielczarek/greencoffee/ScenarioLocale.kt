package com.github.aleksandermielczarek.greencoffee

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ScenarioLocale(val language: String = "", val country: String = "", val variant: String = "")
