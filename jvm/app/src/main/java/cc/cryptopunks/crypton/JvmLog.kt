package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import kotlin.math.absoluteValue

object JvmLog : Log {

    private class Data(
        val packageName: String,
        val className: String,
        val ref: String,
        val message: String
    ) {
        override fun toString(): String {
            val space = LABEL_SIZE -
                    packageName.length -
                    className.length -
                    ref.length

            val formattedPackage =
                if (space > 0) String(CharArray(space) { ' ' }) + packageName
                else packageName.removeRange(0 until space.absoluteValue)

            return "$ref $formattedPackage $className: $message"
        }
    }


    override fun invoke(
        label: String,
        level: Log.Level,
        message: Any
    ) = label.split(".").run {
        Data(
            packageName = filter { it.first().isLowerCase() }.joinToString(".").removePrefix("cc.cryptopunks.crypton."),
            className = filter { it.first().isUpperCase() }.joinToString(".").split("@").first(),
            ref = last().split("@").last(),
            message = message.toString()
        )
    }.let(::println)

    private fun String.formatLabel() = removePrefix("cc.cryptopunks.crypton.")
        .run { split('@') }
        .run { if (size > 1) get(1) + " " + get(0) else get(0) }
        .let { label ->
            if (label.length > LABEL_SIZE)
                label.removeRange(0..(label.length - LABEL_SIZE)) else
                String(CharArray(LABEL_SIZE - label.length) { ' ' }) + label
        }

    private const val LABEL_SIZE = 50
}
