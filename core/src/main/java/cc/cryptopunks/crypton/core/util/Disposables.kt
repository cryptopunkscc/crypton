package cc.cryptopunks.crypton.core.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

class ModelDisposable : Disposables by Disposables()
class ViewDisposable : Disposables by Disposables()

interface Disposables:
    Disposable,
    DisposableContainer {

    fun addAll(vararg disposable: Disposable): Boolean
    fun clear()

    companion object : () -> Disposables by { Impl() }

    private class Impl(
        private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    ) : Disposables,
        Disposable by compositeDisposable,
        DisposableContainer by compositeDisposable {

        override fun addAll(vararg disposable: Disposable): Boolean = compositeDisposable.addAll(*disposable)
        override fun clear(): Unit = compositeDisposable.clear()
    }
}