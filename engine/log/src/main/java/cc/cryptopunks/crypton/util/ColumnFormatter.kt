package cc.cryptopunks.crypton.util

import kotlin.math.absoluteValue

fun columnFormatter(
    initial: List<Int> = emptyList()
): List<String?>.() -> List<String> {
    var columns = initial
    return {
        columns = columns.spreadTo(this)
        formatColumns(columns)
    }
}

private fun List<Int>.spreadTo(strings: List<String?>) = strings.mapIndexed { index, string ->
    maxOf(
        getOrNull(index) ?: 0,
        string?.length ?: 0
    )
}

private fun List<String?>.formatColumns(columns: List<Int>) = mapIndexed { index, string ->
    (string ?: "").formatColumn(columns[index])
}

private fun String.formatColumn(width: Int): String {
    val space = width - length
    return if (space > 0)
        plus(String(CharArray(space) { ' ' })) else
        removeRange((length - space.absoluteValue) until length)
}
