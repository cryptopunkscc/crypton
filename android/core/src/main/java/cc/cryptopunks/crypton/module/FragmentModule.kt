package cc.cryptopunks.crypton.module

import android.os.Bundle
import cc.cryptopunks.crypton.component.FragmentComponent
import cc.cryptopunks.crypton.fragment.CoreFragment

class FragmentModule(
    override val fragment: CoreFragment
) : FragmentComponent {
    override val arguments: Bundle get() = fragment.arguments!!
    override val fragmentManager get() = fragment.childFragmentManager
}

fun CoreFragment.fragmentComponent() = FragmentModule(
    fragment = this
)