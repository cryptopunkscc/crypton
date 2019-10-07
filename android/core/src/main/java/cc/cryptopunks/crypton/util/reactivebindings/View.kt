package cc.cryptopunks.crypton.util.reactivebindings

import android.view.View
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.invoke
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

internal class ViewClicks(
    private val view: View
) : ViewPublisher<Unit>() {

    override fun onSubscribed(subscriber: Subscriber<in Unit>) {
        view.setOnClickListener { subscriber.onNext(Unit) }
    }

    override fun onCanceled() {
        view.setOnClickListener(null)
    }
}

fun View.clicks(): Publisher<Unit> = ViewClicks(this)

suspend fun View.bind(property: Kache<Long>) = clicks().asFlow().collect {
    property { plus(1) }
}

fun View.flowClicks(): Flow<Unit> = callbackFlow {
    setOnClickListener { channel.offer(Unit) }
    awaitClose { setOnClickListener(null) }
}