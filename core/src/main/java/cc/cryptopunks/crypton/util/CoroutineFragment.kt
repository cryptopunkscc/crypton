package cc.cryptopunks.crypton.util

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment :
    Fragment(),
    Scopes.View {

    private var internalCoroutineContext: CoroutineContext? = null
    override val coroutineContext: CoroutineContext get() = internalCoroutineContext!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        internalCoroutineContext = MainScope().coroutineContext
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        internalCoroutineContext?.cancel()
        internalCoroutineContext = null
        super.onDestroyView()
    }
}