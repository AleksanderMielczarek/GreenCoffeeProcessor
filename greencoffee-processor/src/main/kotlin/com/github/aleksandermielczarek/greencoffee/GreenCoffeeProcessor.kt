package com.github.aleksandermielczarek.greencoffee

import java.nio.file.Path
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class GreenCoffeeProcessor : AbstractProcessor() {

    private val scenarios = Scenarios()

    private lateinit var errors: Errors
    private lateinit var programmer: Programmer
    private lateinit var writer: Writer
    private lateinit var androidTestPath: Path

    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(GreenCoffee::class.java.canonicalName)
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf(Arguments.OPTIONS_APP_FOLDER)
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        val arguments = Arguments(processingEnv)
        val typeHelper = TypeHelper(processingEnv)

        errors = Errors(processingEnv)
        programmer = Programmer(typeHelper)
        writer = Writer(processingEnv, errors)

        val androidTestPathHelper = AndroidTestPath(processingEnv, arguments, errors)
        androidTestPath = androidTestPathHelper.getAndroidTestPath()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        processGreenCoffee(roundEnv)
        return true
    }

    private fun processGreenCoffee(roundEnv: RoundEnvironment) {
        roundEnv.getElementsAnnotatedWith(GreenCoffee::class.java)
                .filter { it.kind == ElementKind.CLASS }
                .map { it as TypeElement }
                .forEach { createTestImpl(it) }
    }

    private fun createTestImpl(abstractTest: TypeElement) {
        val greenCoffee = GreenCoffeeData.fromGreenCoffee(abstractTest.getAnnotation(GreenCoffee::class.java))
        val scenarios = scenarios.getScenarios(greenCoffee, androidTestPath)
        val code = programmer.writeCode(abstractTest, greenCoffee, scenarios)
        writer.writeFile(code)
    }
}