package cc.cryptopunks.crypton.util

import kotlin.math.absoluteValue

data class Column(
    val text: String? = null,
    val type: Type = when (text) {
        "-" -> Type.Line
        null -> Type.Blank
        else -> Type.Left
    },
) {
    enum class Type { Left, Right, Center, Blank, Line }
}

fun String?.column(type: Column.Type? = null) = type
    ?.let { Column(this, type) }
    ?: Column(this)

fun columnFormatter(
    initial: List<Int> = emptyList(),
): List<Any?>.() -> List<String> {
    var columns = initial
    return {
        map {
            when (it) {
                is String? -> Column(it)
                is Column -> it
                else -> throw IllegalArgumentException()
            }
        }.run {
            columns = columns.spreadTo(this)
            formatColumns(columns)
        }
    }
}

private fun List<Int>.spreadTo(strings: List<Column>) = strings.mapIndexed { index, column ->
    if (index == size - 1) column.text?.length ?: 0
    else maxOf(
        getOrNull(index) ?: 0,
        column.text?.length ?: 0
    )
}

private fun List<Column>.formatColumns(columns: List<Int>) = prepareColumns(columns)
    .mapIndexed { index, column -> column.formatColumn(columns[index]) }

private fun List<Column>.prepareColumns(columns: List<Int>) = when {
    size < columns.size -> plus((size..columns.size).map { Column() })
    else -> this
}

private fun Column.formatColumn(width: Int): String {
    val text = text ?: ""
    val space = width - text.length
    return when {
        space > 0 -> when (type) {
            Column.Type.Line -> (0 until width).joinToString("") { "-" }
            Column.Type.Blank -> blankString(width)
            Column.Type.Right -> blankString(space) + text
            Column.Type.Center -> blankString(space / 2) + text + blankString(space / 2)
            else -> text + blankString(space)
        }
        else -> text.removeRange((text.length - space.absoluteValue) until text.length)
    }
}

private fun blankString(space: Int) = String(CharArray(space) { ' ' })
