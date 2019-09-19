package cc.cryptopunks.crypton.util

import androidx.annotation.IdRes
import androidx.navigation.NavController
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

interface Navigate : (@IdRes Int) -> Unit {

    interface Output : Flow<Int>

    @FlowPreview
    @ExperimentalCoroutinesApi
    private class Impl : Navigate, Output {
        private val channel = BroadcastChannel<Int?>(Channel.CONFLATED)

        override fun invoke(actionId: Int) {
            channel.offer(actionId)
        }


        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Int>) {
            channel.asFlow()
                .filterNotNull()
                .onEach { channel.offer(null) }
                .collect(collector)
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @dagger.Module
    class Module {
        private val impl = Impl()
        @Provides
        fun navigate(): Navigate = impl
        @Provides
        fun output(): Output = impl
    }

    interface Component {
        val navigate: Navigate
        val navigateOutput: Output
    }
}

suspend fun Navigate.Output.bind(navController: NavController) = collect { id ->
    navController.navigate(id)
}