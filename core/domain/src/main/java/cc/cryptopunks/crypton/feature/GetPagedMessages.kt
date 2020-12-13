package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.pagedMessages
import cc.cryptopunks.crypton.feature

internal fun getPagedMessages() = feature(

    handler = { out, _: Get.PagedMessages ->
        pagedMessages.get()?.let { out(it) }
    }
)
