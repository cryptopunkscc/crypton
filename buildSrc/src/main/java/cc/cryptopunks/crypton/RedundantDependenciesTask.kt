package cc.cryptopunks.crypton

open class RedundantDependenciesTask : AbstractDependenciesTask() {

    override fun Map<String, Set<String>>.onModuleDependencies() {

        mapToModules().getRedundantDependencies().map {
            it.name to it.redundant
        }.forEach {
            logger.quiet(it.toString())
        }
    }
}

fun Map<String, Set<String>>.mapToModules(): MutableMap<String, Module> =
    map { (name, dependencies) ->
        name to Module(
            name = name,
            dependencies = dependencies.toMutableSet(),
            nestedDependencies = mutableSetOf()
        )
    }.toMap().toMutableMap()

fun Map<String, Module>.getRedundantDependencies() = run {
    fillNestedDependencies()
    findRedundantDependencies()
    values.filter { it.redundant.isNotEmpty() }
}

fun Map<String, Module>.fillNestedDependencies(
    list: MutableList<String> = keys.toMutableList(),
    removed: MutableSet<String> = mutableSetOf()
) {
    while (list.isNotEmpty()) {
        getValue(list.removeAt(0)).run {
            fillNestedDependencies(dependencies.toMutableList(), removed)
            nestedDependencies += dependencies.flatMap { getValue(it).allDependencies }
        }
    }
}

private fun Map<String, Module>.findRedundantDependencies() {
    values.forEach { dep ->
        dep.run {
            redundant += dependencies.filter(nestedDependencies::contains)
        }
    }
}

data class Module(
    val name: String,
    val dependencies: MutableSet<String>,
    val nestedDependencies: MutableSet<String>,
    val redundant: MutableSet<String> = mutableSetOf()
) {
    val allDependencies get() = dependencies + nestedDependencies
}

