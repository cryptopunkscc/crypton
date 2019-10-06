package cc.cryptopunks.crypton.fragment

class DependenciesFragment<D> : CoreFragment() {
    private var component: D? = null

    fun init(init: (DependenciesFragment<D>) -> D): D =
        component ?: init(this).also { component = it }
}