package crypton.ops.util

import java.io.File

const val VERSION_CODE = "version.code"
const val VERSION_NAME = "version.name"
const val VERSION_HASH = "version.hash"

const val MAJOR = 0
const val MINOR = 1
const val PATCH = 2
const val BUILD_VERSION = 0

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

internal fun File.getVersion(): List<Int> = readText().trim().split(".").map(String::toInt)

private fun List<Int>.increment(index: Int) = (0 until index)
    .map { getOrNull(it) ?: 0 } + getOrElse(index) { 0 }.inc()
