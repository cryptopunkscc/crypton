package cc.cryptopunks.crypton.util

import androidx.annotation.IdRes
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

object OptionItemSelected {

    interface Input {
        suspend operator fun invoke(@IdRes idRes: Int)
    }

    interface Output : Flow<Int>

    @FlowPreview
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    class Broadcast : Input, Output {

        private val channel = BroadcastChannel<Int>(1)

        override suspend fun invoke(idRes: Int) {
            channel.offer(idRes)
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Int>) {
            channel.consumeEach { collector.emit(it) }
        }
    }

    @FlowPreview
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @dagger.Module
    class Module {
        private val broadcast = Broadcast()

        @Provides
        fun input(): Input = broadcast

        @Provides
        fun output(): Output = broadcast
    }

    interface Component {
        val onOptionItemSelected: Input
        val optionItemSelections: Output
    }
}