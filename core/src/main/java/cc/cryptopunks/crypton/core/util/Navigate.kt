package cc.cryptopunks.crypton.core.util

import androidx.annotation.IdRes
import androidx.navigation.NavController
import cc.cryptopunks.crypton.common.RxPublisher
import cc.cryptopunks.crypton.core.module.FeatureScope
import cc.cryptopunks.crypton.core.util.ext.isEmpty
import dagger.Binds
import dagger.Module
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

interface Navigate : (@IdRes Int) -> Unit {

    interface Publisher : RxPublisher<Int>

    @Module
    interface Bindings {
        @Binds
        fun navigate(navigationBus: NavigationBus): Navigate
        @Binds
        fun publisher(navigationBus: NavigationBus): Publisher
    }

    interface Component {
        val navigate: Navigate
        val navigationPublisher: Publisher
    }
}

@FeatureScope
class NavigationBus @Inject constructor(): Navigate, Navigate.Publisher {

    private val cache = AtomicReference<Int>()
    private val processor = PublishProcessor.create<Int>()

    override fun invoke(actionId: Int) = synchronized(this) {
        when (!processor.hasSubscribers()) {
            true -> cache.set(actionId)
            else -> processor.onNext(actionId)
        }
    }

    override fun subscribe(subscriber: Subscriber<in Int>) = synchronized(this) {
        processor.subscribe(subscriber)
        if (!cache.isEmpty)
            processor.onNext(cache.getAndSet(null))
    }
}

fun Observable<Int>.subscribe(navController: NavController): Disposable = subscribe { id ->
    navController.navigate(id)
}