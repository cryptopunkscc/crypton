package cc.cryptopunks.crypton.component

import androidx.fragment.app.FragmentManager
import cc.cryptopunks.crypton.util.BaseFragment

interface FragmentComponent {
    val fragment: BaseFragment
    val fragmentManager: FragmentManager
}