package cc.cryptopunks.crypton

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

class CryptonPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            val context = extensions.create("crypton", CryptonExtension::class.java)
            tasks {
                register("modulesUml", ModuleUmlTask::class.java) {
                    extension = context
                    group = "documentation"
                }
                register("redundantDependencies", RedundantDependenciesTask::class.java) {
                    group = "help"
                }
            }
        }
    }
}


