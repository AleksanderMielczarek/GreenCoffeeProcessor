package com.github.aleksandermielczarek.greencoffee

import com.squareup.kotlinpoet.*
import java.util.*
import javax.lang.model.element.TypeElement

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
class Programmer(private val typeHelper: TypeHelper) {

    fun writeCode(test: TypeElement, greenCoffee: GreenCoffeeData, scenarios: Int): FileSpec {
        return FileSpec.builder(typeHelper.getPackage(test), "GreenCoffee${typeHelper.getName(test)}")
                .apply { IntRange(0, scenarios - 1).map { addType(createSingleTestImpl(test, greenCoffee, it)) } }
                .build()
    }

    private fun createSingleTestImpl(test: TypeElement, greenCoffee: GreenCoffeeData, index: Int): TypeSpec {
        return TypeSpec.classBuilder("GreenCoffee${typeHelper.getName(test)}${index + 1}")
                .superclass(typeHelper.getTypeName(test))
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
                        .addStatement("[$index]")
                        .unindent()
                        .build())
                .addAnnotation(ClassName("android.support.test.filters", "LargeTest"))
                .addAnnotation(AnnotationSpec.builder(ClassName("org.junit.runner", "RunWith"))
                        .addMember(CodeBlock.of("%T::class", ClassName("android.support.test.runner", "AndroidJUnit4")))
                        .build())
                .build()
    }

    private fun <T> createListFromArray(array: List<T>): String {
        return "listOf(${createVarargFromArray(array)})"
    }

    private fun <T> createVarargFromArray(array: List<T>) = array.joinToString(", ") { "\"$it\"" }
}