package cc.cryptopunks.crypton.context

import java.io.InputStream
import java.io.OutputStream
import kotlin.random.Random

object AesGcm {

    data class Url(
        val url: String,
        val secure: Secure,
    )

    class Secure(
        val iv: ByteArray = randomIv(),
        val key: ByteArray = randomKey()
    )

    interface Sys {

        fun encrypt(
            inputStream: InputStream,
            secure: Secure
        ): InputStream

        fun decrypt(
            outputStream: OutputStream,
            secure: Secure
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
    secure.encode()
).joinToString("")

fun AesGcm.Secure.encode(): String =
    String(iv) + String(key)

fun String.decodeAesGcmUrl(): AesGcm.Url = split(":|#")
    .let { (scheme, link, ivKey) ->
        require(scheme == AES_GCM)
        AesGcm.Url(
            url = "$HTTPS:$link",
            secure = AesGcm.Secure(
                key = ivKey.takeLast(64).toByteArray(),
                iv = ivKey.dropLast(64).toByteArray()
            )
        )
    }
