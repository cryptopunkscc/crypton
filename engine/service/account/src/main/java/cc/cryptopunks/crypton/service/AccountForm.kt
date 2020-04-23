package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.util.TextField

interface AccountForm {

    object ServiceName : Form.Field.Id<TextField>
    object UserName : Form.Field.Id<TextField>
    object Password : Form.Field.Id<TextField>
    object ConfirmPassword : Form.Field.Id<TextField>
    object OnClick

    data class Error(val message: String?)

    fun Form.account() = Account(
        address = Address(
            local = UserName.get().toString(),
            domain = ServiceName.get().toString()
        ),
        password = Password.get()
    )
}