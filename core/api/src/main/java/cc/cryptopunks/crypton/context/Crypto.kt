package cc.cryptopunks.crypton.context

import java.io.InputStream
import java.io.OutputStream
import kotlin.random.Random

object Crypto {

    enum class Mode(val id: Int) { Encrypt(1), Decrypt(2) }

    interface Sys {

        fun encodeBase64(array: ByteArray): String

        fun decodeBase64(string: String): ByteArray

        fun transform(
            stream: InputStream,
            secure: AesGcm.Secure,
            mode: Mode
        ): InputStream

        fun transform(
            stream: OutputStream,
            secure: AesGcm.Secure,
            mode: Mode
        ): OutputStream
    }
}

object AesGcm {

    data class Link(
        val url: String,
        val secure: Secure = Secure(),
    )

    data class Secure(
        val iv: ByteArray = randomIv(),
        val key: ByteArray = randomKey()
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Secure

            if (!iv.contentEquals(other.iv)) return false
            if (!key.contentEquals(other.key)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = iv.contentHashCode()
            result = 31 * result + key.contentHashCode()
            return result
        }
    }

    private fun randomKey(): ByteArray = Random.nextBytes(32)

    private fun randomIv(): ByteArray = Random.nextBytes(16)
}
