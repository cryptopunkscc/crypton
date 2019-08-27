package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.common.HandleError
import cc.cryptopunks.crypton.common.Schedulers
import cc.cryptopunks.crypton.core.util.ActivityStack
import cc.cryptopunks.crypton.core.util.AsyncExecutor

interface UtilsComponent {
    val handleError: HandleError
    val errorPublisher: HandleError.Publisher
    val activityStackCache: ActivityStack.Cache
    val executeAsync: AsyncExecutor
    val schedulers: Schedulers
}