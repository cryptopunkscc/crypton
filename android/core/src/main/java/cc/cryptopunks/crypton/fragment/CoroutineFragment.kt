package cc.cryptopunks.crypton.fragment

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment : Fragment(), CoroutineScope {

    abstract val modelScope: CoroutineScope
    abstract val actorScope: CoroutineScope

    override val coroutineContext: CoroutineContext
        get() = if (view == null)
            modelScope.coroutineContext else
            actorScope.coroutineContext

    override fun onDestroyView() {
        actorScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }

    override fun onDestroy() {
        actorScope.cancel()
        modelScope.cancel()
        super.onDestroy()
    }
}