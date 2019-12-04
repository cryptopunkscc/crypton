package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import cc.cryptopunks.crypton.util.TypedLog
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

interface Service : () -> Unit, Flow<Any>, FlowCollector<Any> {

    val input: suspend (Any) -> Unit
    fun stop()
    fun destroy()
    override suspend fun emit(value: Any) = Unit
    infix fun bind(other: Service)

    interface Delegate {
        fun onInvoke() = Unit
        fun onStop() = Unit
        fun onDestroy() = Unit
        suspend fun Any.onInput(): Any? = Unit
        suspend fun onCollect(collector: FlowCollector<Any>) = Unit
        fun wrapper(scope: CoroutineScope) = Wrapper(this, scope)

        class Wrapper(
            delegate: Delegate,
            override val scope: CoroutineScope
        ) : Abstract() {

            internal var delegate: Delegate? = delegate
                private set

            override fun onInvoke() {
                delegate?.onInvoke()
            }

            override fun onStop() {
                delegate?.onStop()
            }

            override suspend fun Any.onInput(): Any? =
                delegate?.run { this@onInput.onInput() }

            override suspend fun onCollect(collector: FlowCollector<Any>) {
                delegate?.onCollect(collector)
            }

            override fun onDestroy() {
                delegate?.onDestroy()
                delegate = null
            }
        }
    }

    abstract class Abstract :
        Service,
        Delegate {

        private fun Any.unwrap() = when (this) {
            is Delegate.Wrapper -> delegate!!
            else -> this
        }

        private val log: TypedLog by lazy {
            unwrap().typedLog()
        }
        private val outputChannel = Channel<Any>()
        private val inputChannel = Channel<Any>()
        private var isStarted = false
        protected abstract val scope: CoroutineScope
        override val input: suspend (Any) -> Unit = inputChannel::send

        val out: suspend Any.() -> Unit = {
            log.d("out: $this")
            outputChannel.send(this)
        }

        override suspend fun emit(value: Any) = inputChannel.send(value)

        override fun invoke() {
            if (isStarted) {
                log.d("already started")
                return
            }

            log.d("start")
            isStarted = true
            scope.run {
                launch {
                    inputChannel.consumeAsFlow().collect {
                        it.onInput()
                    }
                }
                invokeOnClose { isStarted = false }
            }
            onInvoke()
        }

        override fun stop() {
            log.d("stop")
            isStarted = false
            scope.coroutineContext.cancelChildren()
            onStop()
        }

        override fun destroy() {
            log.d("destroy")
            scope.cancel()
            onDestroy()
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Any>) {
            log.d("collect ${collector.unwrap()}")
            onCollect(collector)
            log.d(1)
            scope.launch {
                log.d(2)
                invoke()
                log.d(3)
            }.invokeOnCompletion {
                log.d("5 $it")
            }
            log.d(4)
            outputChannel.consumeAsFlow().collect(collector)
        }

        @InternalCoroutinesApi
        override fun bind(other: Service) {
            log.d("bind $other")
            scope.run {
                launch { collect(other) }.invokeOnCompletion {
                    log.d("finish binding with $this, error $it")
                }
                launch { other.collect(this@Abstract) }.invokeOnCompletion {
                    log.d("finish binding with $other, error $it")
                }
            }
        }
    }

    interface Wrapper :
        Service,
        Delegate {

        val wrapper: Delegate.Wrapper

        val out: suspend Any.() -> Unit get() = wrapper.out
        override val input: suspend (Any) -> Unit get() = wrapper.input
        override fun invoke() = wrapper()
        override fun destroy() = wrapper.destroy()
        override fun stop() = wrapper.stop()
        @InternalCoroutinesApi
        override fun bind(other: Service) = wrapper.bind(other)

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Any>) = wrapper.collect(collector)
    }


    class Composite(
        private vararg val services: Service
    ) : Service {

        override val input: suspend (Any) -> Unit = { data ->
            services.map(Service::input).forEach { it(data) }
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Any>) =
            services.asFlow().flattenMerge().collect(collector)

        override fun invoke() =
            services.forEach { it() }

        override fun destroy() =
            services.forEach { it.destroy() }

        override fun stop() =
            services.forEach { it.stop() }

        override fun bind(other: Service) =
            throw NotImplementedError()
    }

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}