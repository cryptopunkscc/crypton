package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.AesGcm
import org.junit.Assert.assertEquals
import org.junit.Test

class AesGcmKtTest {

    @Test
    fun test() {
        val expected = AesGcm.Link("https://test.link/file.txt")

        val encoded = expected.encodeString()

        println(encoded)

        val actual = encoded.decodeAesGcmUrl()

        assertEquals(
            expected,
            actual
        )
    }
}
