package com.github.aleksandermielczarek.greencoffee

import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
import com.squareup.kotlinpoet.*
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
                        .addStatement("%T(\"${greenCoffee.screenshotPath}\")", GreenCoffeeConfig::class)
                        .indent()
                        .addStatement(".withFeatureFromAssets(\"${greenCoffee.featureFromAssets}\")")
                        .addStatement(".scenarios()")
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
        return "listOf(${array.joinToString(", ") { "\"$it\"" }})"
    }
}