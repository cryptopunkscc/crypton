package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.util.Store
import cc.cryptopunks.crypton.util.top

internal fun getTopBindingSelector(
    stack: Store<List<Context>>
): () -> Connectable.Binding? = {
    stack.top()?.binding
}
