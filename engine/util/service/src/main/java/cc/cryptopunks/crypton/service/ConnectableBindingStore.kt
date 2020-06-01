package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import java.lang.ref.WeakReference

fun Connectable.Binding.Store.createBinding(): Connectable.Binding = ConnectableBinding3().also { binding ->
    invoke { plus(WeakReference(binding)) }
}

fun Connectable.Binding.Store.remove(binding: Connectable.Binding) {
    invoke { filter { it.get() != binding } }
}

fun Connectable.Binding.Store.top(): Connectable.Binding? = get().lastOrNull()?.get()

suspend fun Connectable.Binding.Store.cancelAll() {
    reduce { forEach { it.get()?.cancel() }; emptyList() }
}

