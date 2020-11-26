package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.cliCommands
import org.junit.Test

class FeaturesKtTest {

    @Test
    fun cliCommands() {
        cryptonFeatures().cliCommands()
    }
}
