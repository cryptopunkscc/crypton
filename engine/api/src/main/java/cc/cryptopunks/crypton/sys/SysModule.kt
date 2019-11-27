package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.context.Message

class SysModule(
    indicator: Indicator.Sys,
    message: Message.Sys
) : Sys,
    Indicator.Sys by indicator,
    Message.Sys by message