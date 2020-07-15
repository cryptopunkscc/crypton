package cc.cryptopunks.crypton

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.invoke

class CryptonPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            val context = extensions.create("crypton", CryptonExtension::class.java)
            tasks {
                register("redundantDependencies", RedundantDependenciesTask::class.java) {
                    group = "help"
                }
                register("modulesPuml", ModuleUmlTask::class.java) {
                    extension = context
                    group = "documentation"
                }
                register("pumlToSvg", Exec::class.java) {
                    group = "documentation"
                    dependsOn("modulesPuml")
                    commandLine("java", "-jar", "./libs/plantuml.jar", "./docs/modules/puml/*.puml", "-svg", "-o", "../svg/")
                }
            }
        }
    }
}


