package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.RosterEvent
import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS

internal class UserTest : IntegrationTest() {

    override fun init() {
        clients(1..2)
    }

    @Test
    operator fun invoke() {
        fun Client.getEvents() = rosterEventPublisher
            .observable()
            .replay()
            .autoConnect()

        val events1 = client1.getEvents()
        val events2 = client2.getEvents()

        val log = Observable
            .merge(events1, events2)
            .doOnNext(::println)
            .test()

        Observable.just(Unit)
            .map { client1.invite(client2.user) }
            .flatMap { events2 }
            .filter { it is RosterEvent.ProcessSubscribe }
            .map { client2.invited(client1.user) }
            .flatMap { events1 }
            .filter { it is RosterEvent.PresenceSubscribed }
            .map { client1.invited(client2.user) }
            .flatMap { events2 }
            .filter { it is RosterEvent.PresenceSubscribed }
            .timeout(5, SECONDS)
            .blockingFirst()

        assertEquals(
            client1.user.remoteId.withoutResource,
            client2.getContacts().first().remoteId
        )

        assertEquals(
            client2.user.remoteId.withoutResource,
            client1.getContacts().first().remoteId
        )

        log.dispose()
    }

}

