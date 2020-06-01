package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class DropBinding(
    private val scope: CoroutineScope,
    private val stack: Store<List<Context>> = Store(emptyList())
) {
    operator fun invoke() = stack.get().lastOrNull()?.also {
        scope.launch { stack reduce { dropLast(1) } }
    }?.binding
}
