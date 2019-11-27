package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route

interface FeatureCore :
    Core,
    Route.Api,
    OptionItem.Api {

    fun sessionFeature(): SessionCore

    interface Create: () -> FeatureCore
}