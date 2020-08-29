package crypton.ops.util

import java.io.File
import java.io.Reader

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

fun Project.writeVersion() {
    versionFile(projectPath).writeText(version.serialize())
}

fun versionFile(projectPath: String): File =
    getOrCreateFile("$projectPath/$VERSION")


fun Version.serialize(): String = listOf(
    build,
    name.joinToString("."),
    hash,
    snapshotHash
).joinToString("") { "$it\n" }

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
            else -> throw Exception("Invalid size: $size")
        }
    }

private fun getOrCreateFile(name: String) = File(name).apply {
    if (!exists()) createNewFile()
}

fun increment(path: String, index: Int) = File(path).increment(index)

fun File.increment(index: Int): List<Int> = getVersion().increment(index).apply {
    writeText(formatVersion().plus("\n"))
}

internal fun List<Int>.formatVersion() = joinToString(".")

internal fun File.getVersion(): List<Int> = readText().getVersion()

internal fun String.getVersion(): List<Int> = trim().split(".").run {
    try {
        map(String::toInt)
    } catch (e: NumberFormatException) {
        listOf(0)
    }
}

fun List<Int>.increment(index: Int) = (0 until index)
    .map { getOrNull(it) ?: 0 } + getOrElse(index) { 0 }.inc()
