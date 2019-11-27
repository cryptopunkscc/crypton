package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.annotation.FeatureScope
import cc.cryptopunks.crypton.context.OptionItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@FeatureScope
@FlowPreview
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class OptionItemBroadcast @Inject constructor() : OptionItem.Select,
    OptionItem.Output {

    private val channel = BroadcastChannel<Int?>(1)

    override suspend fun invoke(idRes: Int) = channel.run {
        send(idRes)
        send(null)
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Int>) = channel
        .asFlow().filterNotNull().collect(collector)
}