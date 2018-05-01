package com.github.aleksandermielczarek.greencoffee

import com.google.common.base.CaseFormat

class ReflectiveScenarioConfig(private val scenarioConfig: Any) {

    private val scenarioConfigClass = Class.forName("com.mauriciotogneri.greencoffee.ScenarioConfig")
    private val scenarioMethod = scenarioConfigClass.getDeclaredMethod("scenario")

    fun scenario(): ReflectiveScenario {
        return ReflectiveScenario(scenarioMethod(scenarioConfig))
    }

    fun camelCaseName(): String {
        return scenario().name()
                .replace(" ", "_")
                .replace(Regex("[^a-zA-Z0-9_]"), "")
                .toLowerCase()
                .let { CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, it) }
    }
}