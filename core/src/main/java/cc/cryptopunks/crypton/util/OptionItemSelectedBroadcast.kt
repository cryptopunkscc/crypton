package cc.cryptopunks.crypton.util

import androidx.annotation.IdRes
import dagger.Provides
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
        suspend operator fun invoke(@IdRes idRes: Int)
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

    @FlowPreview
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @dagger.Module
    class Module {
        private val impl = Impl()

        @Provides
        fun input(): Input = impl

        @Provides
        fun output(): Output = impl
    }

    interface Component {
        val onOptionItemSelected: Input
        val optionItemSelections: Output
    }
}