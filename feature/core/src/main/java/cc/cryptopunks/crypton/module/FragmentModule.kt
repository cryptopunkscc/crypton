package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.FragmentComponent
import cc.cryptopunks.crypton.util.BaseFragment

class FragmentModule(
    override val fragment: BaseFragment
) : FragmentComponent {
    override val fragmentManager get() = fragment.childFragmentManager
}

fun BaseFragment.fragmentComponent() = FragmentModule(
    fragment = this
)