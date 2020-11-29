package cc.cryptopunks.crypton.aesgcm

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import javax.crypto.Cipher
import kotlin.random.Random

class AesGcmKtTest {

    @Test
    fun test() {
        val expected = "test string"

        val iv = Random.nextBytes(16)
        val key = Random.nextBytes(32)

        val encoded = ByteArrayInputStream(expected.toByteArray())
            .withAes(iv, key, Cipher.ENCRYPT_MODE)
            .readBytes()

        val actual = ByteArrayInputStream(encoded)
            .withAes(iv, key, Cipher.DECRYPT_MODE)
            .readBytes()
            .decodeToString()

        assertEquals(
            expected,
            actual
        )
    }
}
