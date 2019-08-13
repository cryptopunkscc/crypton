package cc.cryptopunks.crypton.app.util

import io.reactivex.disposables.Disposable

interface DisposableDelegate : Disposable {

    val disposable: Disposable

    override fun dispose() = disposable.dispose()

    override fun isDisposed() = disposable.isDisposed

}