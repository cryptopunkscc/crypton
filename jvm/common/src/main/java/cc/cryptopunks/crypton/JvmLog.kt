package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import kotlin.math.absoluteValue

object JvmLog : Log {

    private const val PREFIX = "cc.cryptopunks.crypton"

    private data class Data(
        val packageName: String,
        val className: String,
        val ref: String,
        val thread: String,
        val message: String
    ) {
        override fun toString(): String = copy(
            packageName = packageName.formatColumn(PACKAGE_COLUMN_WIDTH),
            className = className.formatColumn(CLASS_COLUMN_WIDTH),
            ref = ref.formatColumn(OBJECT_COLUMN_WIDTH),
            thread = thread.formatColumn(THREAD_COLUMN_WIDTH)
        ).run {
            "$ref $packageName $className $thread : $message"
        }

        fun String.formatColumn(width: Int): String {
            val space = width - length
            return if (space > 0)
                plus(String(CharArray(space) { ' ' })) else
                removeRange((length - space.absoluteValue) until length)
        }
    }

    override fun invoke(
        label: String,
        level: Log.Level,
        message: Any
    ) = label.split(".").let { chunks ->
        Data(
            packageName = chunks
                .filter { it.first().isLowerCase() }
                .joinToString(".")
                .removePrefix(PREFIX)
                .removePrefix("."),
            className = chunks
                .filter { it.first().isUpperCase() }
                .joinToString(".")
                .split("@")
                .first(),
            ref = chunks
                .last()
                .split("@")
                .last(),
            thread = Thread.currentThread().name
                .removePrefix(PREFIX)
                .removePrefix("."),
            message = message.toString()
        )
    }.let(::println)

    private const val OBJECT_COLUMN_WIDTH = 10
    private const val PACKAGE_COLUMN_WIDTH = 12
    private const val CLASS_COLUMN_WIDTH = 36
    private const val THREAD_COLUMN_WIDTH = 36
}
