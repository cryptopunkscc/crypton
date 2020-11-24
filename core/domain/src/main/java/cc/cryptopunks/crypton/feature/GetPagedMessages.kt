package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.feature

internal fun getPagedMessages() = feature(

    handler = { out, _: Get.PagedMessages ->
        pagedMessage.get()?.let { out(it) }
    }
)
