package com.github.aleksandermielczarek.greencoffee

import java.io.InputStream
import java.util.*

class GreenCoffeeConfig(screenshotOnFail: Boolean) {

    private val greenCoffeeConfigClass = Class.forName("com.mauriciotogneri.greencoffee.GreenCoffeeConfig")
    private val greenCoffeeConfigConstructor = greenCoffeeConfigClass.getDeclaredConstructor(Boolean::class.javaObjectType)
    private val withFeatureFromInputStreamMethod = greenCoffeeConfigClass.getDeclaredMethod("withFeatureFromInputStream", InputStream::class.java)
    private val withTagsMethod = greenCoffeeConfigClass.getDeclaredMethod("withTags", String::class.java, Array<String>::class.java)
    private val scenariosMethod = greenCoffeeConfigClass.getDeclaredMethod("scenarios", Array<Locale>::class.java)

    private val greenCoffeeConfig: Any

    init {
        greenCoffeeConfig = greenCoffeeConfigConstructor.newInstance(screenshotOnFail)
    }

    fun withFeatureFromInputStream(inputStream: InputStream): GreenCoffeeConfig {
        withFeatureFromInputStreamMethod(greenCoffeeConfig, inputStream)
        return this
    }

    fun withTags(tags: List<String>): GreenCoffeeConfig {
        when {
            tags.size == 1 -> withTagsMethod(greenCoffeeConfig, tags.first())
            tags.size > 1 -> withTagsMethod(greenCoffeeConfig, tags.first(), tags.subList(1, tags.size).toTypedArray())
        }
        return this
    }

    fun scenarios(locales: List<Locale>): List<ScenarioConfig> {
        val scenarios = scenariosMethod(greenCoffeeConfig, locales.toTypedArray()) as List<*>
        return scenarios.map { ScenarioConfig(it!!) }
    }
}