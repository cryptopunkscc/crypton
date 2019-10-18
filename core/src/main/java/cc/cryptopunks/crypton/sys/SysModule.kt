package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.entity.Message

class SysModule(
    message: Message.Sys
) : Sys.Component,
    Message.Sys by message