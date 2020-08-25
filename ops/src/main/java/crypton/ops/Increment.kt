package crypton.ops

import java.io.File

const val MAJOR = 0
const val MINOR = 1
const val PATCH = 2
const val BUILD_VERSION = 0

fun increment(path: String, index: Int) = File(path).run {
    writeText(getVersion().increment(index).joinToString(".").plus("\n"))
}

private fun File.getVersion(): List<Int> = readText().trim().split(".").map(String::toInt)

private fun List<Int>.increment(index: Int) = (0 until index)
    .map { getOrNull(it) ?: 0 } + getOrElse(index) { 0 }.inc()

