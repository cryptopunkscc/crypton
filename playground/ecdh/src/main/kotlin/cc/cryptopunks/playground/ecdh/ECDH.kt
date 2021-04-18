package cc.cryptopunks.playground.ecdh

import java.nio.ByteBuffer
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement

/**
 * Initialization of [ECDH] context
 */
fun ECDH(
    keyPair: KeyPair = generateEphemeralKeypair()
) = ECDH(

    key = keyPair,

    agreement = KeyAgreement.getInstance("ECDH").apply {
        init(keyPair.private)
    },

    ellipticCurve = KeyFactory.getInstance("EC"),

    sha256 = MessageDigest.getInstance("SHA-256"),
)

fun generateEphemeralKeypair(): KeyPair = KeyPairGenerator
    .getInstance("EC")
    .apply { initialize(256) }
    .generateKeyPair()

/**
 * The cryptographic context required for Elliptic Curve Diffie Hellman key agreement
 */

class ECDH internal constructor(
    internal val key: KeyPair,
    internal val agreement: KeyAgreement,
    internal val ellipticCurve: KeyFactory,
    internal val sha256: MessageDigest,
)

val ECDH.publicKey: ByteArray get() = key.public.encoded

fun ECDH.performKeyAgreement(
    publicKey: ByteArray
): Boolean = runCatching {
    agreement.doPhase(generatePublic(publicKey), true)
}.isSuccess

fun ECDH.buildFinalKey(publicKey: ByteArray): ByteArray = sha256.run {

    listOf(
        key.public.encoded,
        generatePublic(publicKey).encoded
    ).map(ByteBuffer::wrap).sorted().forEach { key ->
        update(key)
    }

    update(agreement.generateSecret())

    digest()
}


// Util

internal fun ECDH.generatePublic(bytes: ByteArray): PublicKey =
    ellipticCurve.generatePublic(X509EncodedKeySpec(bytes))!!

internal fun ECDH.generatePrivate(bytes: ByteArray): PrivateKey =
    ellipticCurve.generatePrivate(X509EncodedKeySpec(bytes))!!

fun String.hexToBinary(): ByteArray = TODO()

