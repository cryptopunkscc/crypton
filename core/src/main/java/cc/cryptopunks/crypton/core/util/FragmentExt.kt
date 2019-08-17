package cc.cryptopunks.crypton.core.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun <reified F : Fragment> FragmentActivity.fragment(
    tag: String? = null,
    create: () -> F = { F::class.java.newInstance() }
): F = (tag ?: F::class.java.toString()).let { fragmentTag ->
    supportFragmentManager
        .findFragmentByTag(fragmentTag) as? F
        ?: create().also {
            supportFragmentManager
                .beginTransaction()
                .add(it, fragmentTag)
                .commit()
        }
}