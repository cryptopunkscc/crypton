package cc.cryptopunks.crypton.context

import java.nio.CharBuffer

data class Password(
    val byteArray: ByteArray
) : CharSequence {
    constructor(charSequence: CharSequence) : this(charSequence.map { it.toByte() }.toByteArray())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Password
        if (!byteArray.contentEquals(other.byteArray)) return false
        return true
    }

    override fun hashCode() = byteArray.contentHashCode()

    override val length = byteArray.size

    override fun get(index: Int) = byteArray[index].toChar()

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = CharBuffer.wrap(
        (startIndex..endIndex).map { byteArray[it].toChar() }.toCharArray()
    )

    companion object {
        val Empty = Password(ByteArray(0))
    }
}

fun ByteArray.toPassword() = Password(this)
