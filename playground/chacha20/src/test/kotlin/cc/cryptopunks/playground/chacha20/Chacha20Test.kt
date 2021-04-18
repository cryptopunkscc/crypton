package cc.cryptopunks.playground.chacha20

import org.junit.Test
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.ChaCha20ParameterSpec
import javax.crypto.spec.SecretKeySpec


class Chacha20Test {

    @Test
    fun test() {
        val plainText = "This is a plain text which will be encrypted by ChaCha20 Algorithm"

        val keyGenerator: KeyGenerator = KeyGenerator.getInstance("ChaCha20")
        keyGenerator.init(256)

        // Generate Key
        val key: SecretKey = keyGenerator.generateKey()

        System.out.println("Original Text  : $plainText")

        val cipherText: ByteArray = encrypt(plainText.toByteArray(), key)
        System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(cipherText))

        val decryptedText: String = decrypt(cipherText, key)
        println("DeCrypted Text : $decryptedText")
    }
}

fun encrypt(plaintext: ByteArray, key: SecretKey): ByteArray {
    val nonceBytes = ByteArray(12)
    val counter = 5

    // Get Cipher Instance
    val cipher: Cipher = Cipher.getInstance("ChaCha20")

    // Create ChaCha20ParameterSpec
    val paramSpec = ChaCha20ParameterSpec(nonceBytes, counter)

    // Create SecretKeySpec
    val keySpec = SecretKeySpec(key.encoded, "ChaCha20")

    // Initialize Cipher for ENCRYPT_MODE
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec)

    // Perform Encryption
    return cipher.doFinal(plaintext)
}

@Throws(Exception::class)
fun decrypt(cipherText: ByteArray?, key: SecretKey): String {
    val nonceBytes = ByteArray(12)
    val counter = 5

    // Get Cipher Instance
    val cipher: Cipher = Cipher.getInstance("ChaCha20")

    // Create ChaCha20ParameterSpec
    val paramSpec = ChaCha20ParameterSpec(nonceBytes, counter)

    // Create SecretKeySpec
    val keySpec = SecretKeySpec(key.encoded, "ChaCha20")

    // Initialize Cipher for DECRYPT_MODE
    cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec)

    // Perform Decryption
    val decryptedText: ByteArray = cipher.doFinal(cipherText)
    return String(decryptedText)
}