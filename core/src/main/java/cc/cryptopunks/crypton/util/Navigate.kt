package cc.cryptopunks.crypton.util

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.navigation.NavController
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

interface Navigate :
        (@IdRes Int) -> Unit,
        (@IdRes Int, Any) -> Unit {

    interface Output : Flow<Data>

    data class Data(
        @IdRes val idRes: Int,
        val param: Any? = null
    )

    @FlowPreview
    @ExperimentalCoroutinesApi
    private class Impl : Navigate, Output {
        override fun invoke(actionId: Int, param: Any) {
            channel.offer(Data(actionId, param))
        }

        private val channel = BroadcastChannel<Data?>(Channel.CONFLATED)

        override fun invoke(actionId: Int) {
            channel.offer(Data(actionId))
        }


        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Data>) {
            channel.asFlow()
                .filterNotNull()
                .onEach { channel.offer(null) }
                .collect(collector)
        }
    }

    @dagger.Module
    class Module : Component {
        private val impl = Impl()
        override val navigate: Navigate @Provides get() = impl
        override val navigateOutput: Output @Provides get() = impl
    }

    interface Component {
        val navigate: Navigate
        val navigateOutput: Output
    }
}

suspend fun Navigate.Output.bind(navController: NavController) = collect { data ->
    val bundle = when (data.param) {
        is Bundle -> data.param
        is Parcelable -> Bundle().apply { putParcelable(null, data.param) }
        else -> null
    }
    navController.navigate(
        data.idRes,
        bundle
    )
}