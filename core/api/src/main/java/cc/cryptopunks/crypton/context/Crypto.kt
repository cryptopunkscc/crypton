package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.hexToBytes
import cc.cryptopunks.crypton.util.toHex
import java.io.InputStream
import java.io.OutputStream
import kotlin.random.Random

val RootScope.cryptoSys: Crypto.Sys by dep()

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

private const val HTTPS = "https"
private const val AES_GCM = "aesgcm"

fun AesGcm.Link.encodeString(): String =
    listOf(
        "$AES_GCM:",
        url.removePrefix("$HTTPS:"),
        "#",
        secure.iv.toHex(),
        secure.key.toHex(),
    ).joinToString("")

fun String.decodeAesGcmUrl(): AesGcm.Link = this
    .split(":", limit = 2)
    .let { (scheme, rest) ->
        require(scheme == AES_GCM)
        val ivKey = split("#").last()
        val link = rest.dropLast(ivKey.length + 1)

        AesGcm.Link(
            url = "$HTTPS:$link",
            secure = AesGcm.Secure(
                key = ivKey.takeLast(64).hexToBytes(),
                iv = ivKey.dropLast(64).hexToBytes(),
            )
        )
    }

fun AesGcm.Link.encodeUri(): URI.Data =
    url.parseUriData().copy(
        scheme = AES_GCM,
        fragment = secure.run { iv.toHex() + key.toHex() }
    )

fun URI.Data.decodeAesGcm(): AesGcm.Link = AesGcm.Link(
    url = copy(
        scheme = HTTPS,
        fragment = ""
    ).format(),
    secure = fragment.parseAesGcmSecure()
)

fun String.parseAesGcmSecure() = AesGcm.Secure(
    key = takeLast(64).hexToBytes(),
    iv = dropLast(64).hexToBytes()
)
