package com.github.aleksandermielczarek.greencoffee

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ScenarioLocale(val language: String = "", val country: String = "", val variant: String = "")
