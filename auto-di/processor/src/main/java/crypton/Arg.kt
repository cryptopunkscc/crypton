package crypton

import javax.lang.model.element.VariableElement

data class Arg(
    val name: String,
    val type: TypeName
)

fun List<Arg>.toMap() = map { it.type to it.name }.toMap()

fun ProcessContext.arg(element: VariableElement): Arg = element.run {
    Arg(
        name = simpleName.toString(),
        type = typeName(element.asType())
    )
}