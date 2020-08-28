package crypton.ops.util

import java.io.File

data class Project(
    val projectPath: String = "",
    val version: Version = Version()
)

fun project(projectPath: Any) = project(projectPath.toString())

fun project(projectPath: String) = Project(
    projectPath = projectPath,
    version = versionFile(projectPath).reader().parseVersion()
)

fun Project.path(file: String) = "$projectPath/$file"

fun Project.file(file: String) = File(path(file))
