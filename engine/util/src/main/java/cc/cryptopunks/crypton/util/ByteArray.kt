package cc.cryptopunks.crypton.util

import java.nio.ByteBuffer

//fun Short.toByteArray() = byteArrayOf(
//    ((toInt() ushr 8) and 0xFF).toByte(),
//    (toInt() and 0xFF).toByte()
//)
//
//fun Int.toByteArray() = byteArrayOf(
//    ((this ushr 16) and 0xFFF).toByte(),
//    ((this ushr 8) and 0xFFF).toByte(),
//    (this and 0xFFF).toByte()
//)
//
//fun Long.toByteArray() = byteArrayOf(
//    ((this ushr 24) and 0xFFFF).toByte(),
//    ((this ushr 16) and 0xFFFF).toByte(),
//    ((this ushr 8) and 0xFFFF).toByte(),
//    (this and 0xFFFF).toByte()
//)

fun Short.toByteArray(): ByteArray = ByteBuffer.allocate(Short.SIZE_BYTES).putShort(this).array()
fun Int.toByteArray(): ByteArray = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array()
fun Long.toByteArray(): ByteArray = ByteBuffer.allocate(Long.SIZE_BYTES).putLong(this).array()

fun ByteArray.getShort() = ByteBuffer.wrap(this).short
fun ByteArray.getInt() = ByteBuffer.wrap(this).int
fun ByteArray.getLong() = ByteBuffer.wrap(this).long
