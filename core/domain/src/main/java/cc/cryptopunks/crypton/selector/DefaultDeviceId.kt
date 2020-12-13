package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.deviceSys

fun RootScope.defaultDeviceId(account: Address) = "${deviceSys.deviceId()}-${account.local}"
