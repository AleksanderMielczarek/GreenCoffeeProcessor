package com.github.aleksandermielczarek.greencoffee

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.*
import javax.lang.model.element.TypeElement

class Programmer(private val typeHelper: TypeHelper) {

    fun writeCode(abstractTest: TypeElement, greenCoffee: GreenCoffeeData, scenarios: List<ReflectiveScenarioConfig>): FileSpec {
        return FileSpec.builder(typeHelper.getPackage(abstractTest), "GreenCoffee${typeHelper.getName(abstractTest)}")
                .apply { scenarios.withIndex().forEach { addType(createSingleTestImpl(abstractTest, greenCoffee, it)) } }
                .build()
    }

    private fun createSingleTestImpl(abstractTest: TypeElement, greenCoffee: GreenCoffeeData, indexedScenario: IndexedValue<ReflectiveScenarioConfig>): TypeSpec {
        return TypeSpec.classBuilder("${typeHelper.getName(abstractTest)}_${indexedScenario.value.camelCaseName()}")
                .superclass(typeHelper.getTypeName(abstractTest))
                .addSuperclassConstructorParameter(CodeBlock.builder()
                        .addStatement("%T(${greenCoffee.screenshotOnFail})", ClassName("com.mauriciotogneri.greencoffee", "GreenCoffeeConfig"))
                        .indent()
                        .addStatement(".withFeatureFromAssets(\"${greenCoffee.featureFromAssets}\")")
                        .apply {
                            if (greenCoffee.tags.isNotEmpty()) {
                                addStatement(".withTags(${createVarargFromArray(greenCoffee.tags)})")
                            }
                        }
                        .addStatement(".scenarios(${greenCoffee.locales.joinToString(", ") { "%T(\"${it.language}\", \"${it.country}\", \"${it.variant}\")" }})", *Collections.nCopies(greenCoffee.locales.size, Locale::class).toTypedArray())
                        .apply {
                            if (greenCoffee.includeScenarios.isNotEmpty()) {
                                addStatement(".filter { ${createListFromArray(greenCoffee.includeScenarios)}.contains(it.scenario().name()) }")
                            }
                            if (greenCoffee.excludeScenarios.isNotEmpty()) {
                                addStatement(".filter { !${createListFromArray(greenCoffee.excludeScenarios)}.contains(it.scenario().name()) }")
                            }
                        }
                        .addStatement("[${indexedScenario.index}]")
                        .unindent()
                        .build())
                .build()
    }

    private fun <T> createListFromArray(array: List<T>): String {
        return "listOf(${createVarargFromArray(array)})"
    }

    private fun <T> createVarargFromArray(array: List<T>) = array.joinToString(", ") { "\"$it\"" }
}