package com.github.aleksandermielczarek.greencoffee

class Scenario(private val scenario: Any) {

    private val scenarioClass = Class.forName("com.mauriciotogneri.greencoffee.Scenario")
    private val nameMethod = scenarioClass.getDeclaredMethod("name")

    fun name(): String {
        return nameMethod(scenario) as String
    }
}