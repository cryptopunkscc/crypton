package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.HandleError
import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.util.ActivityStack
import cc.cryptopunks.crypton.util.AsyncExecutor

interface UtilsComponent {
    val handleError: HandleError
    val errorPublisher: HandleError.Publisher
    val activityStackCache: ActivityStack.Cache
    val executeAsync: AsyncExecutor
    val schedulers: Schedulers
}