package crypton

import javax.inject.Inject
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

data class Property(
    val name: String,
    val type: TypeName,
    val args: List<Arg>? = null
) {
    companion object {
        val Empty = Property(
            name = "TODO()",
            type = TypeName()
        )
    }
}

fun Collection<Property>.getProviders() = map { it.type to it }

operator fun PropertyContext.get(type: TypeName): Property =
    props[type] ?: deps[type] ?: injections[type] ?: Property.Empty

fun ProcessContext.properties(element: TypeElement): List<Property> = element.members
    .filter { it.getAnnotation(External::class.java) == null }
    .map { executableElement -> property(executableElement) }

fun ProcessContext.injectProps(): List<Property> = roundEnv.getElementsAnnotatedWith(Inject::class.java)
    .map { it as ExecutableElement }
    .map { property(it) }

private fun ProcessContext.property(element: ExecutableElement): Property = when (element.kind) {
    ElementKind.CONSTRUCTOR -> constructor(element)
    ElementKind.METHOD -> if (
        element.simpleName.contains("get") &&
        element.parameters.isEmpty()
    )
        getter(element) else
        method(element)
    else -> throw Exception("Unsupported kind: ${element.kind}")
}

fun ProcessContext.getter(element: ExecutableElement) = Property(
    name = element.simpleName.toString().removePrefix("get").run {
        replaceFirst(
            oldChar = first(),
            newChar = first().toLowerCase()
        )
    },
    type = typeName(element.returnType)
)

private fun ProcessContext.method(element: ExecutableElement) = Property(
    name = element.simpleName.toString(),
    type = typeName(element.returnType),
    args = element.parameters.map { parameter ->
        arg(parameter)
    }
)

private fun ProcessContext.constructor(element: ExecutableElement) = Property(
    name = element.enclosingElement.toString(),
    type = typeName(
        (element.enclosingElement as TypeElement)
            .takeIf { it.getAnnotation(AsInterface::class.java) != null }
            ?.run { processingEnv.typeUtils.asElement(interfaces.first()).asType() }
            ?: element.enclosingElement.asType()
    ),
    args = element.parameters.map { parameter ->
        arg(parameter)
    }
)

fun ProcessContext.depProps(): List<Property> = roundEnv.getElementsAnnotatedWith(Dependency::class.java)
    .map { it as TypeElement }
    .map { property(it) }

private fun ProcessContext.property(element: TypeElement) = Property(
    name = element.toString() + "Impl",
    type = typeName(element.asType()),
    args = depListOf(element).map { dep ->
        Arg(
            name = dep.name,
            type = dep.type
        )
    }
)
