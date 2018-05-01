package com.github.aleksandermielczarek.greencoffee

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

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