package cc.cryptopunks.crypton.component

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import cc.cryptopunks.crypton.fragment.CoreFragment

interface FragmentComponent {
    val fragment: CoreFragment
    val fragmentManager: FragmentManager
    val arguments: Bundle
}

