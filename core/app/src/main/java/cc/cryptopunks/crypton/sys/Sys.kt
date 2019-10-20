package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.entity.Indicator
import cc.cryptopunks.crypton.entity.Message

object Sys {
    interface Component :
        Indicator.Sys,
        Message.Sys
}