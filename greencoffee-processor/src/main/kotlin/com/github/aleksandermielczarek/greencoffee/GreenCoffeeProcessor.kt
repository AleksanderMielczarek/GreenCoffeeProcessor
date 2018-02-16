package com.github.aleksandermielczarek.greencoffee

import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


/**
 * Created by Aleksander Mielczarek on 13.02.2018.
 */
class GreenCoffeeProcessor : AbstractProcessor() {

    companion object {
        const val OPTIONS_APP_FOLDER = "appFolder"
    }

    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(GreenCoffee::class.java.canonicalName)
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(OPTIONS_APP_FOLDER)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(GreenCoffee::class.java)
                .filter { it.kind == ElementKind.CLASS }
                .map { it as TypeElement }
                .forEach {
                    val impls = createTestImpls(it, it.getAnnotation(GreenCoffee::class.java))
                    val impl = createTestImpl(it, impls)
                    writeTestImpl(impl)
                }
        return true
    }

    private fun createTestImpls(test: TypeElement, greenCoffee: GreenCoffee): List<TypeSpec> {
        val scenarios = countScenarios(greenCoffee)
        return IntRange(0, scenarios - 1).map { createSingleTestImpl(test, greenCoffee, it) }
    }

    private fun createTestImpl(test: TypeElement, impls: List<TypeSpec>): FileSpec {
        return FileSpec.builder(processingEnv.elementUtils.getPackageOf(test).qualifiedName.toString(), "GreenCoffee${test.simpleName}")
                .apply {
                    impls.forEach { impl -> addType(impl) }
                }
                .build()
    }

    private fun writeTestImpl(test: FileSpec) {
        val kaptGeneratedDirPath = processingEnv.options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")
                ?: run {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
                    throw IllegalStateException()
                }
        val kaptGeneratedDir = File(kaptGeneratedDirPath)
        if (!kaptGeneratedDir.parentFile.exists()) {
            kaptGeneratedDir.parentFile.mkdirs()
        }
        test.writeTo(kaptGeneratedDir)
    }

    private fun countScenarios(greenCoffee: GreenCoffee): Int {
        return GreenCoffeeConfig(greenCoffee.screenshotPath)
                .withFeatureFromInputStream(featureFile(greenCoffee))
                .scenarios()
                .filter {
                    if (greenCoffee.includeScenarios.isNotEmpty()) {
                        greenCoffee.includeScenarios.contains(it.scenario().name())
                    } else {
                        true
                    }
                }
                .filter {
                    if (greenCoffee.excludeScenarios.isNotEmpty()) {
                        !greenCoffee.excludeScenarios.contains(it.scenario().name())
                    } else {
                        true
                    }
                }
                .count()
    }

    private fun featureFile(greenCoffee: GreenCoffee): InputStream {
        val coffeePath = Paths.get(processingEnv.filer.createSourceFile("GreenCoffeeProcessor_${System.currentTimeMillis()}").toUri())
        val appName = processingEnv.options[OPTIONS_APP_FOLDER] ?: "app"
        val testPath = coffeePath.find { Files.isDirectory(it) && it.endsWith(appName) } ?: run {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find app directory")
            throw IllegalStateException()
        }
        return Files.newInputStream(testPath.resolve(Paths.get("src", "androidTest", greenCoffee.featureFromAssets)))
    }

    private fun createSingleTestImpl(test: TypeElement, greenCoffee: GreenCoffee, index: Int): TypeSpec {
        return TypeSpec.classBuilder("GreenCoffee${test.simpleName}${index + 1}")
                .superclass(test.asType().asTypeName())
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

    private fun <T> createListFromArray(array: Array<T>): String {
        return "listOf(${array.joinToString(", ") { "\"$it\"" }})"
    }
}