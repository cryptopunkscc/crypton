package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Feature

interface FeatureCore :
    Api.Core,
    Feature.Core {

    fun sessionFeature(): SessionCore

    interface Create: () -> FeatureCore
}