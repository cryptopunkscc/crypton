package crypton

import com.squareup.kotlinpoet.*

data class PropertyContext(
    val log: (Any?) -> Unit,
    val property: Property,
    val deps: Map<TypeName, Property>,
    val injections: Map<TypeName, Property>,
    val props: Map<TypeName, Property>
)

fun Component.context() = PropertyContext(
    log = log,
    property = Property.Empty,
    deps = deps
        .getProviders()
        .flatten()
        .toMap(),
    injections = injections
        .getProviders()
        .toMap(),
    props = props
        .getProviders()
        .toMap()
).also(log)

fun PropertyContext.of(type: TypeName): PropertyContext = plus(get(type))

operator fun PropertyContext.plus(prop: Property): PropertyContext = copy(
    property = prop,
    props = props.filterValues { it.name != prop.name },
    deps = deps.filterValues { it.name != prop.name },
    injections = injections.filterValues { it.name != prop.name }
)

private fun PropertyContext.factory(
    params: Map<TypeName, String> = emptyMap()
): CodeBlock = buildCodeBlock {
    add(property.name)
    property.args?.run {
        add("(")
        map { arg ->
            buildCodeBlock {
                add("${arg.name} = ")
                params[arg.type]
                    ?.let { add(it) }
                    ?: add(of(arg.type).factory(params))
            }.toString()
        }.takeIf {
            it.isNotEmpty()
        }?.reduce { acc, s ->
            "$acc, $s"
        }?.let {
            add(it)
        }
        add(")")
    }
}

fun PropertyContext.propSpec() = PropertySpec
    .builder(
        property.name,
        property.type.className
    )
    .addModifiers(KModifier.OVERRIDE)
    .delegate(of(property.type).factory().lazy())
    .build()


fun PropertyContext.funSpec() = FunSpec
    .builder(property.name)
    .apply {
        property.args!!.forEach { arg ->
            addParameter(
                name = arg.name,
                type = arg.type.className
            )
        }
    }
    .returns(property.type.className)
    .addCode(of(property.type).factory(property.args!!.toMap()).returns())
    .addModifiers(KModifier.OVERRIDE)
    .build()

fun Component.constructorSpec() = FunSpec
    .constructorBuilder()
    .apply {
        deps.forEach { dep ->
            addParameter(
                dep.name,
                dep.type.className
            )
        }
    }
    .build()

fun Component.constructorPropertiesSpec() = deps.map { prop ->
    PropertySpec
        .builder(
            prop.name,
            prop.type.className
        )
        .initializer(prop.name)
        .addModifiers(KModifier.PRIVATE)
        .build()
}