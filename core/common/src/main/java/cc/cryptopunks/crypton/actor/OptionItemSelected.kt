package cc.cryptopunks.crypton.actor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow

object OptionItemSelected {

    interface Input {
        suspend operator fun invoke(idRes: Int)
    }

    interface Output : Flow<Int>

    @FlowPreview
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    private class Impl : Input, Output {

        private val channel = BroadcastChannel<Int>(1)

        override suspend fun invoke(idRes: Int) {
            channel.offer(idRes)
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Int>) {
            channel.asFlow().collect(collector)
        }
    }

    class Module: Component {
        private val impl = Impl()
        override val onOptionItemSelected: Input get() = impl
        override val optionItemSelections: Output get() = impl
    }

    interface Component {
        val onOptionItemSelected: Input
        val optionItemSelections: Output
    }
}