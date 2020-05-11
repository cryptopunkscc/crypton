package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.util.Store
import cc.cryptopunks.crypton.util.top

internal class GetTopBinding(
    private val stack: Store<List<Context>>
) {
    operator fun invoke() = stack.top()?.binding
}
