package com.github.aleksandermielczarek.greencoffee

class ReflectiveScenario(private val scenario: Any) {

    private val scenarioClass = Class.forName("com.mauriciotogneri.greencoffee.Scenario")
    private val tagsField = scenarioClass.getDeclaredField("tags")
    private val nameMethod = scenarioClass.getDeclaredMethod("name")

    init {
        tagsField.isAccessible = true
    }

    @Suppress("UNCHECKED_CAST")
    fun tags(): List<String> {
        return tagsField.get(scenario) as List<String>
    }

    fun name(): String {
        return nameMethod(scenario) as String
    }
}