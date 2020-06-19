package cc.cryptopunks.crypton.model

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.util.Store

internal class Form(fields: Map<Account.Field, CharSequence> = emptyMap()) :
    Store<Map<Account.Field, CharSequence>>(fields)
