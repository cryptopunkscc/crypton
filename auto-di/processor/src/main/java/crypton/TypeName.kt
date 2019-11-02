package crypton

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

data class TypeName(
    val name: String = "",
    val pkg: String = ""
)

val TypeName.className
    get() = ClassName(
        packageName = pkg,
        simpleNames = listOf(name)
    )

val TypeName.companion
    get() = ClassName(
        packageName = pkg,
        simpleNames = listOf(name, "Companion")
    )

fun ProcessContext.typeName(element: TypeMirror): TypeName = let {

    val pkg = processingEnv.typeUtils
        .asElement(element)
        ?.let(processingEnv.elementUtils::getPackageOf)
        .toString()

    val name = element.toString()
        .removePrefix(pkg)
        .removePrefix(".")

    TypeName(
        name = name.split("<")[0],
        pkg = pkg
    )
}

fun ProcessContext.interfacesOf(element: TypeElement): Set<TypeName> = element.interfaces
    .map { typeName(it) }
    .toSet()