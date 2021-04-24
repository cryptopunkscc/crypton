package cc.cryptopunks.playground.bolt

import org.junit.Test

class CryptoKtTest : BoltHandshake.Crypto by JavaCrypto {

    @Test
    fun generateKeyTest() {
        generateKey()
    }
}