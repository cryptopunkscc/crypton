package cc.cryptopunks.crypton

import kotlinx.coroutines.CancellationException
import java.lang.ref.WeakReference

fun Connectable.Binding.Store.createBinding(): Connectable.Binding = ConnectableBinding().also { binding ->
    invoke { plus(WeakReference(binding)) }
}

fun Connectable.Binding.Store.remove(binding: Connectable.Binding) {
    invoke { filter { it.get() != binding } }
}

fun Connectable.Binding.Store.top(): Connectable.Binding? = get().lastOrNull()?.get()

suspend fun Connectable.Binding.Store.cancelAll(cause: CancellationException? = null) {
    reduce { forEach { it.get()?.cancel(cause) }; emptyList() }
}

