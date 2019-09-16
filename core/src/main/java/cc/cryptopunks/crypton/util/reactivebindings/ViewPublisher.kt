package cc.cryptopunks.crypton.util.reactivebindings

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

internal abstract class ViewPublisher<T> : Publisher<T> {

    var subscriber: Subscriber<in T>? = null
        private set

    private val subscription = object : Subscription {
        override fun cancel() {
            this@ViewPublisher.cancel()
        }

        override fun request(n: Long) {
            /*no-op*/
        }
    }

    final override fun subscribe(subscriber: Subscriber<in T>) {
        this.subscriber = subscriber
        subscriber.onSubscribe(subscription)
        onSubscribed(subscriber)
    }

    abstract fun onSubscribed(subscriber: Subscriber<in T>)

    fun cancel() {
        subscriber = null
        onCanceled()
    }

    abstract fun onCanceled()

}