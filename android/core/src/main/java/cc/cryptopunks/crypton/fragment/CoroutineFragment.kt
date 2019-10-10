package cc.cryptopunks.crypton.fragment

import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment : Fragment(), CoroutineScope {

    val presentationScope by lazy { Scope.Presentation() }
    val viewScope by lazy { Scope.View() }

    override val coroutineContext: CoroutineContext
        get() = if (view == null)
            presentationScope.coroutineContext else
            viewScope.coroutineContext

    override fun onDestroyView() {
        viewScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewScope.cancel()
        presentationScope.cancel()
        super.onDestroy()
    }
}