package cc.cryptopunks.crypton.smack.integration.test

import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.smack.integration.IntegrationTest
import io.reactivex.Observable
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS

internal class MessagingTest : IntegrationTest() {

    override fun init() {
        clients(1..2)
    }

    @Test
    operator fun invoke() {
        val messages = client2.messagePublisher.observable()
            .replay()
            .autoConnect()

        val test = messages.test()

        Observable.just(Unit)
            .doOnNext {
                client1.sendMessage(
                    client2.user.remoteId,
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