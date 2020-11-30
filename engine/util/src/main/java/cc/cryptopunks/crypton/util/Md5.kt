package cc.cryptopunks.crypton.util

import java.security.MessageDigest

fun String.md5(): String = MD5.digest(toByteArray()).printHexBinary()

private val MD5 = MessageDigest.getInstance("MD5")

private fun ByteArray.printHexBinary(): String =
    asSequence().map(Byte::toInt).fold(StringBuilder(size * 2)) { acc, i ->
        acc.append(HEX_CHARS[i shr 4 and 0xF]).append(HEX_CHARS[i and 0xF])
    }.toString()

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
