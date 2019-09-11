package cc.cryptopunks.crypton.util

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsyncExecutor @Inject constructor(
    private val handleError: HandleError,
    private val runningTasks: RunningTasks
) {

    private val scheduler = Schedulers.single()

    operator fun invoke(
        onError: (Throwable) -> Unit = handleError,
        task: () -> Completable
    ): Unit = invoke<Any>(onError) { task() }(Unit)

    operator fun <A : Any> invoke(
        onError: (Throwable) -> Unit = handleError,
        task: (A) -> Completable
    ): (A) -> Unit = { arg ->
        wrap(task)(arg).subscribe({}, onError)
    }

    fun <A : Any> wrap(
        task: (A) -> Completable
    ): (A) -> Completable = { arg ->
        Single.just(Unit).flatMapCompletable {
            synchronized(runningTasks) {
                runningTasks.add(
                    TaskContext(
                        task = task,
                        arg = arg
                    )
                ).run {
                    publisher ?: PublishProcessor.create<Nothing>().run {
                        publisher = this
                        doOnSubscribe {
                            Single.just(arg)
                                .observeOn(scheduler)
                                .flatMapCompletable(task)
                                .doAfterTerminate {
                                    synchronized(runningTasks) {
                                        runningTasks.remove(context)
                                    }
                                }
                                .subscribe(
                                    ::onComplete,
                                    handleError
                                )
                        }
                    }
                }.let {
                    Completable.fromPublisher(it)
                }
            }
        }
    }
}

data class TaskContext(
    val task: Any,
    val arg: Any
)

data class MutableTaskContext(
    val context: TaskContext,
    private val cache: MutableMap<Any, Publisher<Nothing>>
) {
    private val arg get() = context.arg

    var publisher: Publisher<Nothing>?
        get() = cache[arg]
        set(value) {
            if (value == null) cache.remove(arg)
            else cache[arg] = value
        }
}

@Singleton
class RunningTasks @Inject constructor() {

    private val cache = mutableMapOf<Any, MutableMap<Any, Publisher<Nothing>>>()

    val size get() = cache.size

    val values get() = cache.values

    fun add(context: TaskContext) = MutableTaskContext(
        context = context,
        cache = cache.getOrPut(
            context.task,
            ::mutableMapOf
        )
    )

    fun remove(context: TaskContext): Boolean = context.run {
        cache.get(task)?.let { args ->
            args.remove(arg)?.also {
                if (args.isEmpty()) {
                    cache.remove(task) != null
                }
            }
        }
    } != null

    override fun toString(): String = cache.toMap().toString()
}