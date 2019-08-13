package cc.cryptopunks.crypton.app.util

import cc.cryptopunks.crypton.common.HandleError
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

class AsyncExecutor(
    private val handleError: HandleError,
    private val runningTasks: RunningTasks
) {

    private val scheduler = Schedulers.single()

    operator fun invoke(task: () -> Completable): Unit = invoke<Any> { task() }(Unit)

    operator fun <A : Any> invoke(
        onError: (Throwable) -> Unit = handleError,
        task: (A) -> Completable
    ): (A) -> Unit = { arg ->
        val context = TaskContext(
            task = task,
            arg = arg
        )
        runningTasks.add(context).let { notRunning ->

            if (notRunning) Single.just(arg)
                .observeOn(scheduler)
                .flatMapCompletable(task)
                .doAfterTerminate {
                    runningTasks.remove(context)
                }
                .subscribe(
                    {},
                    onError
                )
        }
    }
}

data class TaskContext(
    val task: Any,
    val arg: Any
)

@Singleton
class RunningTasks @Inject constructor() : MutableMap<Any, MutableSet<Any>> by mutableMapOf() {

    fun add(context: TaskContext): Boolean = context.run {
        getOrPut(task, ::mutableSetOf).add(arg)
    }

    fun remove(context: TaskContext): Boolean = context.run {
        get(task)?.let { args ->
            args.remove(arg).also { removed ->
                if (removed && args.isEmpty()) {
                    remove(task)
                }
            }
        }
    } ?: false

    override fun toString(): String = toMap().toString()
}