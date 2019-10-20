package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.entity.Indicator
import cc.cryptopunks.crypton.entity.Message

class SysModule(
    indicator: Indicator.Sys,
    message: Message.Sys
) : Sys.Component,
    Indicator.Sys by indicator,
    Message.Sys by message