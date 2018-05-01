package com.github.aleksandermielczarek.greencoffee

import java.io.InputStream
import java.util.*

class ReflectiveGreenCoffeeConfig {

    private val greenCoffeeConfigClass = Class.forName("com.mauriciotogneri.greencoffee.GreenCoffeeConfig")
    private val greenCoffeeConfigConstructor = greenCoffeeConfigClass.getDeclaredConstructor(Boolean::class.javaObjectType)
    private val withFeatureFromInputStreamMethod = greenCoffeeConfigClass.getDeclaredMethod("withFeatureFromInputStream", InputStream::class.java)
    private val withTagsMethod = greenCoffeeConfigClass.getDeclaredMethod("withTags", String::class.java, Array<String>::class.java)
    private val scenariosMethod = greenCoffeeConfigClass.getDeclaredMethod("scenarios", Array<Locale>::class.java)

    private val greenCoffeeConfig: Any

    constructor(screenshotOnFail: Boolean) {
        greenCoffeeConfig = greenCoffeeConfigConstructor.newInstance(screenshotOnFail)
    }

    constructor(greenCoffeeConfig: Any) {
        this.greenCoffeeConfig = greenCoffeeConfig
    }

    fun withFeatureFromInputStream(inputStream: InputStream): ReflectiveGreenCoffeeConfig {
        return ReflectiveGreenCoffeeConfig(withFeatureFromInputStreamMethod(greenCoffeeConfig, inputStream))
    }

    fun withTags(tags: List<String>): ReflectiveGreenCoffeeConfig {
        return when {
            tags.size == 1 -> ReflectiveGreenCoffeeConfig(withTagsMethod(greenCoffeeConfig, tags.first()))
            tags.size > 1 -> ReflectiveGreenCoffeeConfig(withTagsMethod(greenCoffeeConfig, tags.first(), tags.subList(1, tags.size).toTypedArray()))
            else -> this
        }
    }

    fun scenarios(locales: List<Locale>): List<ReflectiveScenarioConfig> {
        val scenarios = scenariosMethod(greenCoffeeConfig, locales.toTypedArray()) as List<*>
        return scenarios.map { ReflectiveScenarioConfig(it!!) }
    }
}