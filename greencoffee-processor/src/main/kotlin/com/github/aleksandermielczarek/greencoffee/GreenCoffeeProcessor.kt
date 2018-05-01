package com.github.aleksandermielczarek.greencoffee

import java.nio.file.Path
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class GreenCoffeeProcessor : AbstractProcessor() {

    companion object {
        const val OPTIONS_APP_FOLDER = "appFolder"
    }

    private val scenarioCounter = ScenarioCounter()
    private lateinit var typeHelper: TypeHelper
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
        return mutableSetOf(OPTIONS_APP_FOLDER)
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeHelper = TypeHelper(processingEnv)
        programmer = Programmer(typeHelper)
        writer = Writer(processingEnv)

        val fileHelper = FileHelper(processingEnv)
        androidTestPath = fileHelper.getAndroidTestPath()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(GreenCoffee::class.java)
                .filter { it.kind == ElementKind.CLASS }
                .map { it as TypeElement }
                .forEach {
                    val greenCoffee = GreenCoffeeData.fromGreenCoffee(it.getAnnotation(GreenCoffee::class.java))
                    val scenarios = scenarioCounter.count(greenCoffee, androidTestPath)
                    val code = programmer.writeCode(it, greenCoffee, scenarios)
                    writer.writeFile(code)
                }
        return true
    }
}