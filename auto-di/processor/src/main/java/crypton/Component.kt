package crypton

import com.squareup.kotlinpoet.*
import javax.lang.model.element.TypeElement

data class Component(
    val log: (Any?) -> Unit,
    val name: TypeName = TypeName(),
    val interfaces: Set<TypeName> = emptySet(),
    val deps: List<Dep> = emptyList(),
    val injections: List<Property> = emptyList(),
    val props: List<Property> = emptyList()
)

fun ProcessContext.component(
    log: (Any?) -> Unit,
    element: TypeElement,
    injections: List<Property>
) = Component(
    log = log,
    name = typeName(element.asType()),
    interfaces = interfacesOf(element),
    props = properties(element),
    deps = depListOf(element),
    injections = injections
)

fun Component.build() = FileSpec.builder(
    fileName = name.name + "Impl",
    packageName = name.pkg
)
    .addType(
        TypeSpec
            .classBuilder(name.name + "Impl")
            .primaryConstructor(constructorSpec())
            .addSuperinterfaces(interfaces.map { it.className })
            .apply {
                addProperties(constructorPropertiesSpec())
                val context = context()
                props.map { context + it }.forEach { ctx ->
                    if (ctx.property.args == null)
                        addProperty(ctx.propSpec()) else
                        addFunction(ctx.funSpec())
                }
            }
            .build()
    )
    .build()
    .run {
        StringBuffer().also {
            writeTo(it)
        }
    }