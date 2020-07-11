package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.conference

const val domain = "localhost"
const val pass = "pass"

const val test1 = "test1"
const val test2 = "test2"
const val test3 = "test3"
const val chat = "chat"

val address1 = Address(test1, domain)
val address2 = Address(test2, domain)
val address3 = Address(test3, domain)
val chatAddress = Address(chat, domain).conference()

object Client1
object Client2
object Client3
