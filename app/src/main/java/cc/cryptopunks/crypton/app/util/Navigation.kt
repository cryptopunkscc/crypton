package cc.cryptopunks.crypton.app.util

import androidx.annotation.IdRes
import androidx.navigation.NavController
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.util.concurrent.atomic.AtomicReference

interface Navigation {

    fun navigate(@IdRes actionId: Int)


    data class Data(
        @IdRes val actionId: Int
    )

    interface Bus : Navigation, Publisher<Data>
}


class NavigationBus : Navigation.Bus {

    private val cache = AtomicReference<Navigation.Data>()
    private val processor = PublishProcessor.create<Navigation.Data>()

    override fun navigate(actionId: Int) = synchronized(this) {
        val navigate = Navigation.Data(
            actionId = actionId
        )
        when (!processor.hasSubscribers()) {
            true -> cache.set(navigate)
            else -> processor.onNext(navigate)
        }
    }

    override fun subscribe(subscriber: Subscriber<in Navigation.Data>) = synchronized(this) {
        processor.subscribe(subscriber)
        if (!cache.isEmpty)
            processor.onNext(cache.getAndSet(null))
    }
}

fun Observable<Navigation.Data>.subscribe(navController: NavController): Disposable = subscribe {
    navController.navigate(it.actionId)
}