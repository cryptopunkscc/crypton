package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.util.hexToBytes
import cc.cryptopunks.crypton.util.toHex

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

