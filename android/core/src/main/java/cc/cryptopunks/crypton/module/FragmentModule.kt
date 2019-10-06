package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.FragmentComponent
import cc.cryptopunks.crypton.fragment.CoreFragment

class FragmentModule(
    override val fragment: CoreFragment
) : FragmentComponent {
    override val fragmentManager get() = fragment.childFragmentManager
}

fun CoreFragment.fragmentComponent() = FragmentModule(
    fragment = this
)