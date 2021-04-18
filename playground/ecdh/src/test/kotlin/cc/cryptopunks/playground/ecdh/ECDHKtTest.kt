package cc.cryptopunks.playground.ecdh

import cc.cryptopunks.crypton.util.toHex
import org.junit.Test

class ECDHKtTest {

    @Test
    fun test() {

        val ecdh1 = ECDH()
        val ecdh2 = ECDH()

        ecdh1.performKeyAgreement(ecdh2.publicKey)
        ecdh2.performKeyAgreement(ecdh1.publicKey)

        println(ecdh1.buildFinalKey(ecdh2.publicKey).toHex())
        println(ecdh2.buildFinalKey(ecdh1.publicKey).toHex())
    }
}
