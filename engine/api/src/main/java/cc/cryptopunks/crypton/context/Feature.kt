package cc.cryptopunks.crypton.context

object Feature {
    interface Core :
        Route.Api,
        OptionItem.Api
}