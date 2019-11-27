package cc.cryptopunks.crypton.context

interface Sys :
    Indicator.Sys,
    Message.Sys {

    class Module(
        indicator: Indicator.Sys,
        message: Message.Sys
    ) : Sys,
        Indicator.Sys by indicator,
        Message.Sys by message
}