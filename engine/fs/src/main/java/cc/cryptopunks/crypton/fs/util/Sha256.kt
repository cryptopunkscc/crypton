package cc.cryptopunks.crypton.fs.util

import java.io.File
import java.io.InputStream
import java.security.MessageDigest

internal const val SHA_256 = "SHA-256"

fun sha256MessageDigest() = MessageDigest.getInstance(SHA_256)!!

fun sha256FromFile(path: String) = File(path).sha256()

fun File.sha256() = inputStream().sha256()

fun InputStream.sha256(): ByteArray =
    sha256MessageDigest().apply {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var len = read(buffer)
        while (len > 0) {
            update(buffer, 0, len)
            len = read(buffer)
        }
    }.digest()

fun String.sha256(): ByteArray =
    MessageDigest.getInstance(SHA_256).digest(toByteArray())
