package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handlePurgeDeviceList() = handle { out, _: Exec.PurgeDeviceList ->
    purgeDeviceList()
}
