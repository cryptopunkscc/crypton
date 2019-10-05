package cc.cryptopunks.crypton.util

class DependenciesFragment<D> : BaseFragment() {
    private var component: D? = null

    fun init(init: (DependenciesFragment<D>) -> D): D =
        component ?: init(this).also { component = it }
}