package com.github.aleksandermielczarek.greencoffee

class ScenarioConfig(private val scenarioConfig: Any) {

    private val scenarioConfigClass = Class.forName("com.mauriciotogneri.greencoffee.ScenarioConfig")
    private val scenarioMethod = scenarioConfigClass.getDeclaredMethod("scenario")

    fun scenario(): Scenario {
        return Scenario(scenarioMethod(scenarioConfig))
    }
}