package cc.cryptopunks.crypton

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.TaskAction

abstract class AbstractDependenciesTask : DefaultTask() {

    @TaskAction
    fun exec() {
        getModulesDependencies().onModuleDependencies()
    }

    private fun getModulesDependencies(): Map<String, Set<String>> =
        mutableMapOf<String, MutableSet<String>>().apply {
            project.subprojects.filter(hasGradleFile).forEach { project ->
                if (!contains(project.path)) set(project.path, mutableSetOf())
                project.configurations.filterNot(isTestImplementation).forEach { configuration ->
                    configuration.dependencies.filter(isProjectDependency)
                        .filter { it.moduleName != project.path }
                        .forEach { dependency: Dependency ->
                            getValue(project.path).add(dependency.moduleName)
                        }
                }
            }
        }

    private val isProjectDependency = { dependency: Dependency ->
        dependency.group?.startsWith(project.name) == true
    }

    private val isTestImplementation = { configuration: Configuration ->
        configuration.name.let {
            "testImplementation" in it || "androidTestImplementation" in it
        }
    }

    private val hasGradleFile = { project: Project ->
        project.buildscript.sourceFile?.exists() == true
    }

    private val Dependency.moduleName get() = ":$prefix:$name"

    private val Dependency.prefix
        get() = group?.run {
            removePrefix("${project.name}.").replace(".", ":")
        }

    abstract fun Map<String, Set<String>>.onModuleDependencies()
}
