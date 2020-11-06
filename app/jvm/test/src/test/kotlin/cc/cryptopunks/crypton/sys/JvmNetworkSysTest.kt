package cc.cryptopunks.crypton.sys

import org.junit.Test
import java.net.NetworkInterface

class JvmNetworkSysTest {

    @Test
    fun test() {
        NetworkInterface.getNetworkInterfaces().iterator().forEach { networkInterface ->
            networkInterface.print()
        }
    }

    private fun NetworkInterface.print() {
        println("$index $displayName $isVirtual $isUp")
    }

}
