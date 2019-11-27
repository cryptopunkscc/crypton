package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation

interface FeatureCore : Core, Navigation {
    fun sessionFeature(): SessionCore
    interface Create: () -> FeatureCore
}