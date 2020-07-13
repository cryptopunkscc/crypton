package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.model.Form

internal fun AppScope.handleSetField(
    form: Form
) = handle<Account.Service.Set> {
    form { plus(field to text) }
}
