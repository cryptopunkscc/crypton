package cc.cryptopunks.crypton.app.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.full.createInstance

inline fun <reified F : Fragment> FragmentActivity.fragment(
    tag: String? = null,
    create: () -> F = { F::class.createInstance() }
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