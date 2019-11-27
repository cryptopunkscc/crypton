package cc.cryptopunks.crypton.navigation

import cc.cryptopunks.crypton.annotation.FeatureScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

object OptionItem {

    interface Select {
        suspend operator fun invoke(idRes: Int)
    }

    interface Output : Flow<Int>

    @FeatureScope
    @FlowPreview
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    class Broadcast @Inject constructor() : Select, Output {

        private val channel = BroadcastChannel<Int?>(1)

        override suspend fun invoke(idRes: Int) = channel.run {
            send(idRes)
            send(null)
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Int>) = channel
            .asFlow().filterNotNull().collect(collector)
    }

    interface Api {
        val selectOptionItem: Select
        val optionItemSelections: Output
    }
}