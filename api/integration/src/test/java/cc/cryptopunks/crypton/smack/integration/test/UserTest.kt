package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.crypton.entity.RosterEvent.PresenceSubscribed
import cc.cryptopunks.crypton.entity.RosterEvent.ProcessSubscribe
import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import cc.cryptopunks.crypton.smack.integration.test
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import org.junit.Assert.assertEquals
import org.junit.Test

internal class UserTest : IntegrationTest() {

    override fun init() {
        clients(1..2)
    }

    @Test
    fun invoke() = test(timeout = 5000) {
        val events1 = client1.rosterEventPublisher.openSubscription()
        val events2 = client2.rosterEventPublisher.openSubscription()

        client1.invite(client2.address)
        events2.receiveFiltered { it is ProcessSubscribe }
        client2.invited(client1.address)
        events1.receiveFiltered { it is PresenceSubscribed }
        client1.invited(client2.address)
        events2.receiveFiltered { it is PresenceSubscribed }

        assertEquals(
            client1.address,
            client2.getContacts().first().address
        )

        assertEquals(
            client2.address,
            client1.getContacts().first().address
        )
    }
}

tailrec suspend fun <T> ReceiveChannel<T>.receiveFiltered(filter: (T) -> Boolean): T {
    val received = receive()
    return if (filter(received))
        received else
        receiveFiltered(filter)
}