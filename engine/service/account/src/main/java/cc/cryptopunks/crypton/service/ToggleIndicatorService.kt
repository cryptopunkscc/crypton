package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ApplicationScope
class ToggleIndicatorService @Inject constructor(
    private val scope: Service.Scope,
    private val hasAccounts: HasAccountsSelector,
    private val showIndicator: Indicator.Sys.Show,
    private val hideIndicator: Indicator.Sys.Hide
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        hasAccounts().collect { condition ->
            when (condition) {
                true -> showIndicator()
                false -> hideIndicator()
            }
        }
    }
}