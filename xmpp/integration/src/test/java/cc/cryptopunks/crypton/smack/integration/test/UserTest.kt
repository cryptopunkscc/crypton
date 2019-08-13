package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.xmpp.entities.RosterEvent
import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS

internal class UserTest : IntegrationTest() {

    override fun init() {
        xmpps(1..2)
    }

    @Test
    operator fun invoke() {
        fun Xmpp.getEvents() = rosterEventPublisher
            .observable()
            .replay()
            .autoConnect()

        val events1 = xmpp1.getEvents()
        val events2 = xmpp2.getEvents()

        val log = Observable
            .merge(events1, events2)
            .doOnNext(::println)
            .test()

        Observable.just(Unit)
            .map { xmpp1.invite(xmpp2.user) }
            .flatMap { events2 }
            .filter { it is RosterEvent.ProcessSubscribe }
            .map { xmpp2.invited(xmpp1.user) }
            .flatMap { events1 }
            .filter { it is RosterEvent.PresenceSubscribed }
            .map { xmpp1.invited(xmpp2.user) }
            .flatMap { events2 }
            .filter { it is RosterEvent.PresenceSubscribed }
            .timeout(5, SECONDS)
            .blockingFirst()

        assertEquals(
            xmpp1.user.jid.withoutResource,
            xmpp2.getContacts().first().jid
        )

        assertEquals(
            xmpp2.user.jid.withoutResource,
            xmpp1.getContacts().first().jid
        )

        log.dispose()
    }

}

