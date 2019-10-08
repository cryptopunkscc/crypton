package cc.cryptopunks.crypton.fragment

import androidx.fragment.app.Fragment

class ComponentHolderFragment<D> : Fragment() {
    private var component: D? = null

    fun initComponent(init: (ComponentHolderFragment<D>) -> D): D =
        component ?: init(this).also { component = it }
}