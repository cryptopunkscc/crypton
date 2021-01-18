package cc.cryptopunks.crypton.agent

import java.nio.ByteBuffer

fun Long.toByteArray(): ByteArray = ByteBuffer
    .allocate(Long.SIZE_BYTES)
    .putLong(this)
    .array()

fun Int.toByteArray(): ByteArray = ByteBuffer
    .allocate(Int.SIZE_BYTES)
    .putInt(this)
    .array()

fun ByteArray.toLong(): Long = ByteBuffer
    .allocate(Long.SIZE_BYTES)
    .put(this)
    .apply { flip() }
    .long

fun ByteArray.toInt(): Long = ByteBuffer
    .allocate(Long.SIZE_BYTES)
    .put(this)
    .apply { flip() }
    .long
