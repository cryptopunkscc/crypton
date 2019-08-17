package cc.cryptopunks.crypton.core.util

import java.util.concurrent.atomic.AtomicReference

class DependenciesFragment<D> : BaseFragment() {
    private val componentRef = AtomicReference<D>()

    val component get() = componentRef.get()

    companion object {
        operator fun <D> invoke(dependencies: D) =
            DependenciesFragment<D>().apply {
            componentRef.set(dependencies)
        }
    }
}