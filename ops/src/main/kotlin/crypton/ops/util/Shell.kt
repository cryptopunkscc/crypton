package crypton.ops.util

fun String.execShell(): String {
    val process = Runtime.getRuntime().exec(this)
    val result = process.waitFor()
    return if (result > 0) throw Exception(
        "$this exit code $result\n" +
            process.errorStream.bufferedReader().readText()
    )
    else process.inputStream.bufferedReader().readText().trim()
}
