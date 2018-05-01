package com.mauriciotogneri.greencoffee

class Scenario(
        private val scenarioName: String,
        private val tags: List<String>
) {

    fun name(): String {
        return scenarioName
    }
}