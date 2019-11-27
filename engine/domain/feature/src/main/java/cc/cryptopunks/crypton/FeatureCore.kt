package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.context.Route

interface FeatureCore :
    Core,
    Route.Api,
    OptionItem.Api {

    fun sessionFeature(): SessionCore

    interface Create: () -> FeatureCore
}