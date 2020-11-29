package cc.cryptopunks.crypton.context

import java.io.InputStream
import java.io.OutputStream
import kotlin.random.Random

object AesGcm {
    data class Link(
        val url: String,
        val secure: Secure,
    )

    class Secure(
        val iv: ByteArray = randomIv(),
        val key: ByteArray = randomKey()
    )


    private fun randomKey(): ByteArray = Random.nextBytes(32)

    private fun randomIv(): ByteArray = Random.nextBytes(16)
}

object Crypto {

    interface Sys {

        fun encodeBase64(array: ByteArray): String

        fun decodeBase64(string: String): ByteArray

        fun encrypt(
            inputStream: InputStream,
            secure: AesGcm.Secure
        ): InputStream

        fun decrypt(
            outputStream: OutputStream,
            secure: AesGcm.Secure
        ): OutputStream
    }
}
