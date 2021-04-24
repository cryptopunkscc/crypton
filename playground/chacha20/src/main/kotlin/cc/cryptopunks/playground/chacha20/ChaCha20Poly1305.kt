package cc.cryptopunks.playground.chacha20

import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object ChaCha20Poly1305 {

    // if no nonce, generate a random 12 bytes nonce
    fun encrypt(
        pText: ByteArray,
        key: SecretKey,
        nonce: ByteArray = randomNonce()
    ): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)

        // IV, initialization value with nonce
        val iv = IvParameterSpec(nonce)
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        val encryptedText = cipher.doFinal(pText)

        // append nonce to the encrypted text
        return ByteBuffer.allocate(encryptedText.size + NONCE_LEN)
            .put(encryptedText)
            .put(nonce)
            .array()
    }

    fun decrypt(cText: ByteArray, key: SecretKey?): ByteArray {
        val bb = ByteBuffer.wrap(cText)

        // split cText to get the appended nonce
        val encryptedText = ByteArray(cText.size - NONCE_LEN)
        val nonce = ByteArray(NONCE_LEN)
        bb[encryptedText]
        bb[nonce]
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)
        val iv = IvParameterSpec(nonce)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)

        // decrypted text
        return cipher.doFinal(encryptedText)
    }
}

private const val ENCRYPT_ALGO = "ChaCha20-Poly1305"
private const val NONCE_LEN = 12 // 96 bits, 12 bytes

// 96-bit nonce (12 bytes)
private fun randomNonce(): ByteArray {
    val newNonce = ByteArray(12)
    SecureRandom().nextBytes(newNonce)
    return newNonce
}