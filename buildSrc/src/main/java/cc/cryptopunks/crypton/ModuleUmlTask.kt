package cc.cryptopunks.crypton

import org.gradle.api.tasks.Input
import java.io.File


open class ModuleUmlTask : AbstractDependenciesTask() {

    @Input
    lateinit var extension: CryptonExtension

    override fun Map<String, Set<String>>.onModuleDependencies() {
        when {
            extension.outputs.isNotEmpty() -> {
                mapToModules().apply {
                    fillNestedDependencies()
                    extension.outputs.forEach { (module, output) ->
                        (getValue(module).allDependencies + module).map {
                            getValue(it).run { name to dependencies }
                        }.toMap().writeToFile(output)
                    }
                }
            }
            else -> writeToFile(extension.output)
        }
    }
}

private fun Map<String, Set<String>>.writeToFile(output: String) = File(output).apply {
    createNewFile()
    outputStream().writer().apply {
        startUml()
        keys.groupBy(String::modulePrefix).forEach { (group, modules) ->
            packageOf(group) {
                modules.forEach { module ->
                    appendln(component(module))
                }
            }
        }
        forEach { (module, dependencies) ->
            dependencies.forEach { dependency ->
                addRelation(component(module) to component(dependency))
            }
        }
        endUml()
    }.flush()
}

private val String.modulePrefix get() = split(":").getOrNull(1).let { ":$it" }
