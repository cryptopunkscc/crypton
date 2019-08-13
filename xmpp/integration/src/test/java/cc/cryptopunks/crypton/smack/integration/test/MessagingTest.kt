package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import io.reactivex.Observable
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS

internal class MessagingTest : IntegrationTest() {

    override fun init() {
        xmpps(1..2)
    }

    @Test
    operator fun invoke() {
        val messages = xmpp2.chatMessagePublisher.observable()
            .replay()
            .autoConnect()

        val test = messages.test()

        Observable.just(Unit)
            .doOnNext {
                xmpp1.sendMessage(
                    xmpp2.user.jid,
                    "test"
                )
            }
            .flatMap { messages }
            .doOnNext(::println)
            .timeout(15, SECONDS)
            .blockingFirst()

        with(test) {
            assertNoErrors()
            assertValueCount(1)
        }
    }
}