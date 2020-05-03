package cc.cryptopunks.crypton.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class JobManager<T>(
    override val scope: CoroutineScope,
    override val log: TypedLog? = null,
    override val execute: T.() -> Job
) : AbstractJobManager<T>()

abstract class AbstractJobManager<T> : (T) -> Job {

    protected abstract val scope: CoroutineScope

    protected abstract val execute: (T) -> Job

    protected open val log: TypedLog? = null

    private val channel by lazy {
        Channel<T>(Channel.BUFFERED).also { channel ->
            scope.launch {
                for(arg in channel)
                    executeIfNeeded(arg)
            }
        }
    }

    private val jobs = mutableMapOf<T, Job>()

    val isWorking get() = jobs.isNotEmpty()

    private fun executeIfNeeded(arg: T) = jobs.getOrPut(arg) {
        log?.d("execute $arg")
        execute(arg).apply {
            invokeOnCompletion {
                jobs -= arg
            }
        }
    }

    final override fun invoke(arg: T) = executeIfNeeded(arg)

    suspend fun send(arg: T) {
        log?.d("request execution $arg")
        channel.send(arg)
    }

    fun cancel(arg: T) {
        jobs.remove(arg)?.cancel()
    }
}
