package cc.cryptopunks.crypton.component

import androidx.fragment.app.FragmentManager
import cc.cryptopunks.crypton.fragment.CoreFragment

interface FragmentComponent {
    val fragment: CoreFragment
    val fragmentManager: FragmentManager
}