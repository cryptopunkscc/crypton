package cc.cryptopunks.crypton.util

import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment :
    Fragment(),
    CoroutineScope {

    private val fragmentContext = SupervisorJob() + Dispatchers.Main
    private val viewContext = fragmentContext + Dispatchers.Main

    override val coroutineContext: CoroutineContext
        get() = if (view == null)
            fragmentContext else
            viewContext

    override fun onDestroyView() {
        viewContext.cancelChildren()
        super.onDestroyView()
    }

    override fun onDestroy() {
        fragmentContext.cancel()
        super.onDestroy()
    }
}