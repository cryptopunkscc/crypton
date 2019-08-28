package cc.cryptopunks.crypton.core.util

import cc.cryptopunks.crypton.common.RxBroadcast
import cc.cryptopunks.crypton.core.module.FeatureScope
import dagger.Binds
import dagger.Module
import org.reactivestreams.Publisher
import javax.inject.Inject

interface OptionItemSelected : Publisher<Int> {

    interface Input : (Int) -> Unit

    @FeatureScope
    class Broadcast @Inject constructor() : OptionItemSelected, Input, RxBroadcast<Int>()

    @Module
    interface Bindings {
        @Binds
        fun publisher(broadcast: Broadcast) : OptionItemSelected
        @Binds
        fun input(broadcast: Broadcast) : Input
    }
}