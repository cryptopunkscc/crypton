package cc.cryptopunks.playground.chacha20

import org.junit.Test
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class TestChaCha20Poly1305 {

    @Test
    fun testChaChaPoly() {
        val input = "Java & ChaCha20-Poly1305."
        val cipher = ChaCha20Poly1305
        val key = key // 256-bit secret key (32 bytes)
        println("Input                  : $input")
        println("Input             (hex): " + convertBytesToHex(input.toByteArray()))
        println("\n---Encryption---")
        val cText = cipher.encrypt(input.toByteArray(), key) // encrypt
        println("Key               (hex): " + convertBytesToHex(key.encoded))
        println("Encrypted         (hex): " + convertBytesToHex(cText))
        println("\n---Print Mac and Nonce---")
        val bb = ByteBuffer.wrap(cText)

        // This cText contains chacha20 ciphertext + poly1305 MAC + nonce

        // ChaCha20 encrypted the plaintext into a ciphertext of equal length.
        val originalCText = ByteArray(input.toByteArray().size)
        val nonce = ByteArray(NONCE_LEN) // 16 bytes , 128 bits
        val mac = ByteArray(MAC_LEN) // 12 bytes , 96 bits
        bb[originalCText]
        bb[mac]
        bb[nonce]
        println("Cipher (original) (hex): " + convertBytesToHex(originalCText))
        println("MAC               (hex): " + convertBytesToHex(mac))
        println("Nonce             (hex): " + convertBytesToHex(nonce))
        println("\n---Decryption---")
        println("Input             (hex): " + convertBytesToHex(cText))
        val pText = cipher.decrypt(cText, key) // decrypt
        println("Key               (hex): " + convertBytesToHex(key.encoded))
        println("Decrypted         (hex): " + convertBytesToHex(pText))
        println("Decrypted              : " + String(pText))
    }
}

private const val NONCE_LEN = 12 // 96 bits, 12 bytes
private const val MAC_LEN = 16 // 128 bits, 16 bytes

// https://mkyong.com/java/java-how-to-convert-bytes-to-hex/
private fun convertBytesToHex(bytes: ByteArray): String {
    val result = StringBuilder()
    for (temp in bytes) {
        result.append(String.format("%02x", temp))
    }
    return result.toString()
}

// A 256-bit secret key (32 bytes)
private val key: SecretKey get() {
    val keyGen = KeyGenerator.getInstance("ChaCha20")
    keyGen.init(256, SecureRandom.getInstanceStrong())
    return keyGen.generateKey()
}