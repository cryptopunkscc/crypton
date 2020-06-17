package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.service.Form
import kotlinx.coroutines.launch

internal fun AppScope.handleSetField(
    form: Form
) = handle<Account.Service.Set> {
    launch { form { plus(field to text) } }
}
