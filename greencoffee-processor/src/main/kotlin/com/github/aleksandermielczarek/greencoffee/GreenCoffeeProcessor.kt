package com.github.aleksandermielczarek.greencoffee

import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement


/**
 * Created by Aleksander Mielczarek on 13.02.2018.
 */
class GreenCoffeeProcessor : AbstractProcessor() {

    companion object {
        const val OPTIONS_APP_FOLDER = "appFolder"
    }

    private lateinit var typeHelper: TypeHelper
    private lateinit var fileHelper: FileHelper
    private lateinit var programmer: Programmer
    private lateinit var writer: Writer

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
        fileHelper = FileHelper(processingEnv)
        programmer = Programmer(typeHelper)
        writer = Writer(processingEnv)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(GreenCoffee::class.java)
                .filter { it.kind == ElementKind.CLASS }
                .map { it as TypeElement }
                .forEach {
                    val greenCoffee = GreenCoffeeData.fromGreenCoffee(it.getAnnotation(GreenCoffee::class.java))
                    val scenarios = countScenarios(greenCoffee)
                    val code = programmer.writeCode(it, greenCoffee, scenarios)
                    writer.writeFile(code)
                }
        return true
    }

    private fun countScenarios(greenCoffee: GreenCoffeeData): Int {
        return GreenCoffeeConfig(greenCoffee.screenshotPath)
                .withFeatureFromInputStream(fileHelper.getFeatureFile(greenCoffee))
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
}