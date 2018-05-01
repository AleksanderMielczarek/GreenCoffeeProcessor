package com.github.aleksandermielczarek.greencoffee

import com.google.common.base.CaseFormat
import com.squareup.kotlinpoet.ClassName

class ReflectiveScenarioConfig(private val scenarioConfig: Any) {

    private val supportedAnnotations = mapOf(
            "@Ignore" to ClassName("org.junit", "Ignore"),
            "@FlakyTest" to ClassName("android.support.test.filters", "FlakyTest"),
            "@SmallTest" to ClassName("android.support.test.filters", "SmallTest"),
            "@MediumTest" to ClassName("android.support.test.filters", "MediumTest"),
            "@LargeTest" to ClassName("android.support.test.filters", "LargeTest")
    )

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

    fun annotations(): List<ClassName> {
        return scenario().tags().mapNotNull { convertTagToAnnotation(it) }
    }

    private fun convertTagToAnnotation(tag: String): ClassName? {
        val supportedAnnotation = supportedAnnotations[tag]
        if (supportedAnnotation != null) {
            return supportedAnnotation
        }

        val annotationPackage = getPackage(tag) ?: return null
        val annotationName = getName(tag)
        return ClassName(annotationPackage, annotationName)
    }

    private fun getPackage(tag: String): String? {
        val paths = tag.split(".")
        if (paths.size < 2) {
            return null
        }

        return paths.subList(0, paths.size - 1).joinToString(".") { it.replace("@", "") }
    }

    private fun getName(tag: String): String {
        return tag.split(".").last().replace("@", "")
    }
}