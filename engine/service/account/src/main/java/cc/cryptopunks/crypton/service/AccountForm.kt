package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.util.Form
import cc.cryptopunks.crypton.util.TextField

interface AccountForm {

    object ServiceName : Form.Field.Id
    object UserName : Form.Field.Id
    object Password : Form.Field.Id
    object ConfirmPassword : Form.Field.Id
    object OnClick

    data class Error(val message: String?)

    fun Form.address() = Address(
        local = UserName<TextField>()!!.string,
        domain = ServiceName<TextField>()!!.string
    )

    fun Form.account() = Account(
        address = address(),
        password = Password<TextField>()!!
    )
}