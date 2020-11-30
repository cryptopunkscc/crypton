package cc.cryptopunks.crypton.aesgcm

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File
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

    @Test
    fun test2() {
        val iv = Random.nextBytes(16)
        val key = Random.nextBytes(32)
        val expected = "Yo, this is a test text"

        val inFile = File("aesgcm.in").apply {
            appendText(expected)
            deleteOnExit()
        }

        val outFile = File("aesgcm.out").apply {
            deleteOnExit()
        }

        ByteArrayInputStream(expected.toByteArray())
            .withAes(iv, key, Cipher.ENCRYPT_MODE)
            .copyTo(inFile.outputStream())

        inFile.inputStream()
            .withAes(iv, key, Cipher.DECRYPT_MODE)
            .copyTo(outFile.outputStream())

        val actual = outFile.readText()

        assertEquals(
            expected,
            actual
        )
    }
}
