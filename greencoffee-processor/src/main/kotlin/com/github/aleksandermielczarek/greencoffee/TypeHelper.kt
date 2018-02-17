package com.github.aleksandermielczarek.greencoffee

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
class TypeHelper(private val processingEnv: ProcessingEnvironment) {

    fun getPackage(typeElement: TypeElement): String {
        return processingEnv.elementUtils.getPackageOf(typeElement).qualifiedName.toString()
    }

    fun getName(typeElement: TypeElement): String {
        return typeElement.simpleName.toString()
    }

    fun getTypeName(typeElement: TypeElement): TypeName {
        return typeElement.asType().asTypeName()
    }
}