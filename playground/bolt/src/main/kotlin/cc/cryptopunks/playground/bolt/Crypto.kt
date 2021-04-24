@file:Suppress("FunctionName")

package cc.cryptopunks.playground.bolt

import org.mozilla.android.sync.crypto.HKDF.hkdfExpand
import org.mozilla.android.sync.crypto.HKDF.hkdfExtract
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.interfaces.ECPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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
internal fun sha256(
    input: ByteArray
): ByteArray =
    MessageDigest.getInstance("SHA-256").digest(input)


/**
 * Serialize in Bitcoin's compressed format.
 */
internal fun ByteArray.serializeCompressed(): ByteArray =
    KeyFactory
        .getInstance("EC")
        .generatePublic(X509EncodedKeySpec(this))
        .let { it as ECPublicKey }.w.run {
            byteArrayOf(
                if (affineY.mod(TWO) == ZERO) 2 else 3
            ) + affineX.toByteArray()
        }


/**
 * ECDH(k, rk): performs an Elliptic-Curve Diffie-Hellman operation
 * @param privateEphemeralKey A valid secp256k1 private key,
 * @param publicStaticRemoteKey A valid public key
 * @return The returned value is the SHA256 of the compressed format of the generated point.
 */
internal fun ECDH(
    privateEphemeralKey: ByteArray,
    publicStaticRemoteKey: ByteArray
): ByteArray {
    val ec = KeyFactory.getInstance("EC")
    val ecdh = KeyAgreement.getInstance("ECDH")
    ecdh.init(ec.generatePrivate(X509EncodedKeySpec(privateEphemeralKey)))
    val key = ecdh.doPhase(ec.generatePublic(X509EncodedKeySpec(publicStaticRemoteKey)), true)
    return MessageDigest.getInstance("SHA-256").digest(key.encoded)
}

internal fun HKDF(
    salt: ByteArray,
    ikm: ByteArray
): Pair<ByteArray, ByteArray> {
    val prk = hkdfExtract(salt, ikm)
    val okm = hkdfExpand(prk, byteArrayOf(), 64)
    return prk to okm
}


/**
 * Encrypt plain text using AEAD_CHACHA20_POLY1305 algorithm (IETF variant).
 * Note: this follows the Noise Protocol convention, rather than our normal endian.
 *
 * @param k A 256-bit key.
 * @param n A value used to calculate a 96-bit nonce.
 * @param ad An additional data.
 * @param plaintext - Data for encryption.
 * @return Encrypted cipher text.
 */
internal fun encryptWithAD(
    k: ByteArray,
    n: Long,
    ad: ByteArray,
    plaintext: ByteArray
): ByteArray {
    val cipher = Cipher.getInstance("ChaCha20-Poly1305")
    val key = SecretKeySpec(k, "ChaCha20")
    val nonce = encodeNonce(n)
    val iv = IvParameterSpec(nonce)
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    cipher.updateAAD(ad)
    return cipher.doFinal(plaintext)
}

/**
 * Decrypt cipher text using AEAD_CHACHA20_POLY1305 algorithm (IETF variant).
 * Note: this follows the Noise Protocol convention, rather than our normal endian.
 *
 * @param k A 256-bit key
 * @param n A value used to calculate a 96-bit nonce
 * @param ad An additional data
 * @param ciphertext - An encrypted data.
 * @return Decrypted plain text.
 */
internal fun decryptWithAD(
    k: ByteArray,
    n: Long,
    ad: ByteArray,
    ciphertext: ByteArray
): ByteArray {
    val cipher = Cipher.getInstance("ChaCha20-Poly1305")
    val key = SecretKeySpec(k, "ChaCha20")
    val nonce = encodeNonce(n)
    val iv = IvParameterSpec(nonce)
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    cipher.updateAAD(ad)
    return cipher.doFinal(ciphertext)
}

/**
 * Encode as 32 zero bits, followed by a little-endian 64-bit value.
 */
private fun encodeNonce(n: Long) = ByteBuffer
    .allocate(Int.SIZE_BYTES + Long.SIZE_BYTES)
    .order(ByteOrder.LITTLE_ENDIAN) // TODO verify
    .flip()
    .putInt(0)
    .putLong(4, n)
    .array()

//fun pad16(x: ByteArray) =
//    if (x.size % 16 == 0) byteArrayOf()
//    else ByteArray(16 - (x.size % 16))
