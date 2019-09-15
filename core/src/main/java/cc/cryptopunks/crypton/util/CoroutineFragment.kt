package cc.cryptopunks.crypton.util

import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment :
    Fragment(),
    CoroutineScope {

    private val modelContext = SupervisorJob() + Dispatchers.Main
    private val viewContext = modelContext + Dispatchers.Main

    override val coroutineContext: CoroutineContext
        get() = if (view == null)
            modelContext else
            viewContext

    override fun onDestroyView() {
        viewContext.cancelChildren()
        super.onDestroyView()
    }

    override fun onDestroy() {
        modelContext.cancel()
        super.onDestroy()
    }
}