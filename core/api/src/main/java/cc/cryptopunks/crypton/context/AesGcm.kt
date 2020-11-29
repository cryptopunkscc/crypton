package cc.cryptopunks.crypton.context

import java.io.InputStream
import java.io.OutputStream
import kotlin.random.Random

object AesGcm {

    class Url(
        val url: String,
        val iv: ByteArray = randomIv(),
        val key: ByteArray = randomKey()
    )

    interface Sys {

        fun encrypt(
            inputStream: InputStream,
            iv: ByteArray,
            key: ByteArray
        ): InputStream

        fun decrypt(
            outputStream: OutputStream,
            iv: ByteArray,
            key: ByteArray
        ): OutputStream
    }

    private fun randomKey(): ByteArray = Random.nextBytes(32)

    private fun randomIv(): ByteArray = Random.nextBytes(16)
}

private const val HTTPS = "https"

private const val AES_GCM = "aesgcm"

fun AesGcm.Url.encode(): String = listOf(
    "$AES_GCM:",
    url.removePrefix("$HTTPS:"),
    "#",
    String(iv),
    String(key),
).joinToString("")

fun String.decodeAesGcmUrl(): AesGcm.Url = split(":|#")
    .let { (scheme, link, ivKey) ->
        require(scheme == AES_GCM)
        AesGcm.Url(
            url = "$HTTPS:$link",
            key = ivKey.takeLast(64).toByteArray(),
            iv = ivKey.dropLast(64).toByteArray()
        )
    }
