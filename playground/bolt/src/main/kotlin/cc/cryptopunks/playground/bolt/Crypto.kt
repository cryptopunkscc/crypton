@file:Suppress("FunctionName")

package cc.cryptopunks.playground.bolt

import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement

/**
 * Generates and returns a fresh secp256k1 keypair
 */
fun generateKey(): KeyPair =
    KeyPairGenerator
        .getInstance("EC")
        .apply { initialize(256) }
        .generateKeyPair()
        .run {
            KeyPair(
                pub = public.encoded,
                priv = private.encoded
            )
        }

class KeyPair(
    val pub: ByteArray,
    val priv: ByteArray
)


/**
 * Calculate SHA 256 digest
 */
fun sha256(
    input: ByteArray
): ByteArray =
    MessageDigest.getInstance("SHA-256").digest(input)


/**
 * Serialize in Bitcoin's compressed format.
 */
fun ByteArray.serializeCompressed(): ByteArray = TODO()


/**
 * ECDH(k, rk): performs an Elliptic-Curve Diffie-Hellman operation
 * @param privateEphemeralKey A valid secp256k1 private key,
 * @param publicStaticRemoteKey A valid public key
 * @return The returned value is the SHA256 of the compressed format of the generated point.
 */
fun ECDH(
    privateEphemeralKey: ByteArray,
    publicStaticRemoteKey: ByteArray
): ByteArray {
    val ec = KeyFactory.getInstance("EC")
    val ecdh = KeyAgreement.getInstance("ECDH").apply {
        init(ec.generatePrivate(X509EncodedKeySpec(privateEphemeralKey)))
    }
    val key = ecdh.doPhase(ec.generatePublic(X509EncodedKeySpec(publicStaticRemoteKey)), true)
    return MessageDigest.getInstance("SHA-256").digest(key.encoded)
}

fun HKDF(
    chainingKey: ByteArray,
    es: ByteArray
): Pair<ByteArray, ByteArray> = TODO()

fun encryptWithAD(
    secp256k1PrivateKey: ByteArray,
    nonce: Int,
    handshakeHash: ByteArray,
    text: ByteArray
): ByteArray = TODO()

fun decryptWithAD(
    secp256k1PrivateKey: ByteArray,
    nonce: Int,
    handshakeHash: ByteArray,
    text: ByteArray
): ByteArray = TODO()