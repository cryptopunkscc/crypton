package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun dropBindingInteractor(
    scope: CoroutineScope,
    stack: Store<List<Context>> = Store(emptyList())
): () -> Connectable.Binding? = {
    stack.get().lastOrNull()?.also {
        scope.launch { stack reduce { dropLast(1) } }
    }?.binding
}
