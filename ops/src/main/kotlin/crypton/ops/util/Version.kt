package crypton.ops.util

import java.io.File
import java.io.Reader

const val VERSION_CODE = "version.code"
const val VERSION_NAME = "version.name"
const val VERSION_HASH = "version.hash"
const val VERSION = "version"

const val MAJOR = 0
const val MINOR = 1
const val PATCH = 2
const val BUILD_VERSION = 0

data class Version(
    val build: Int = 0,
    val name: List<Int> = emptyList(),
    val hash: String = "",
    val snapshotHash: String = ""
)

fun Project.write(version: Version) {
    versionFile(projectPath).writeText(version.serialize())
}

fun versionFile(projectPath: String): File =
    getOrCreateFile("$projectPath/$VERSION")


fun Version.serialize(): String = listOf(
    build,
    name.joinToString("."),
    hash,
    snapshotHash
).joinToString("") { "it\n" }

fun Reader.parseVersion(): Version =
    readLines().run {
        when {
            isEmpty() -> Version()
            size >= 4 -> Version(
                build = get(0).toInt(),
                name = get(1).getVersion(),
                hash = get(2),
                snapshotHash = get(3)
            )
            else -> throw Exception()
        }
    }

fun versionCode(projectPath: String) = getOrCreateFile("$projectPath/$VERSION_CODE")

fun versionName(projectPath: String) = getOrCreateFile("$projectPath/$VERSION_NAME")

fun versionHash(projectPath: String) = getOrCreateFile("$projectPath/$VERSION_HASH")

private fun getOrCreateFile(name: String) = File(name).apply {
    if (!exists()) createNewFile()
}

fun increment(path: String, index: Int) = File(path).increment(index)

fun File.increment(index: Int): List<Int> = getVersion().increment(index).apply {
    writeText(formatVersion().plus("\n"))
}

fun File.updateSha(sha: String) = sha.also {
    writeText(sha)
}

internal fun List<Int>.formatVersion() = joinToString(".")

internal fun File.getVersion(): List<Int> = readText().getVersion()

internal fun String.getVersion(): List<Int> = trim().split(".").map(String::toInt)

fun List<Int>.increment(index: Int) = (0 until index)
    .map { getOrNull(it) ?: 0 } + getOrElse(index) { 0 }.inc()
