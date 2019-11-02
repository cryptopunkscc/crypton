package crypton

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

data class Dep(
    val prop: Property,
    val props: List<Property>
) {
    val name: String get() = prop.name
    val type: TypeName get() = prop.type
}

fun ProcessContext.depListOf(element: TypeElement): List<Dep> = element.members
    .filter { it.getAnnotation(External::class.java) != null }
    .map { dep(it) }

private fun ProcessContext.dep(
    element: ExecutableElement
) = Dep(
    prop = getter(element),
    props = properties(processingEnv.typeUtils.asElement(element.returnType) as TypeElement)
)

fun Collection<Dep>.getProviders(): List<List<Pair<TypeName, Property>>> = map { dep ->
    dep.getProviders()
}

private fun Dep.getProviders(): List<Pair<TypeName, Property>> = props.map { prop ->
    prop.type to prop.copy(
        name = name + "." + prop.name
    )
}