package crypton.ops.util

import java.io.File

data class Project(
    val projectPath: String,
    val versionName: List<Int>,
    val versionCode: Int,
    val versionHash: String
)

fun project(projectPath: Any) = project(projectPath.toString())

fun project(projectPath: String) = Project(
    projectPath = projectPath,
    versionName = versionName(projectPath).getVersion(),
    versionCode = versionCode(projectPath).getVersion().first(),
    versionHash = versionHash(projectPath).readText().trim()
)

fun Project.path(file: String) = "$projectPath/$file"

fun Project.file(file: String) = File(path(file))
