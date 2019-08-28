package cc.cryptopunks.crypton.core.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class DisposableActivity :
    AppCompatActivity(),
    DisposableDelegate {

    private val onPauseDisposable = CompositeDisposable()
    private val onStopDisposable = CompositeDisposable()
    private val onDestroyDisposable = CompositeDisposable()

    override val disposable get() = onDestroyDisposable

    open fun CompositeDisposable.onCreate(savedInstanceState: Bundle?): Any = Unit
    open fun CompositeDisposable.onStart(): Any = Unit
    open fun CompositeDisposable.onResume(): Any = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onDestroyDisposable.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        onStopDisposable.onStart()
    }

    override fun onResume() {
        super.onResume()
        onPauseDisposable.onResume()
    }

    override fun onPause() {
        onPauseDisposable.clear()
        super.onPause()
    }

    override fun onStop() {
        onStopDisposable.clear()
        super.onStop()
    }

    override fun onDestroy() {
        onPauseDisposable.dispose()
        onStopDisposable.dispose()
        onDestroyDisposable.dispose()
        super.onDestroy()
    }
}