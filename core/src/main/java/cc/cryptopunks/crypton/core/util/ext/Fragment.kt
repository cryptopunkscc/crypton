package cc.cryptopunks.crypton.core.util.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun <reified F : Fragment> FragmentActivity.fragment(
    tag: String? = null,
    create: () -> F = { F::class.java.newInstance() }
): F = with(supportFragmentManager) {

    val fragmentTag = tag ?: F::class.java.toString()

    findFragmentByTag(fragmentTag) as? F ?: create().also { fragment ->
        beginTransaction().add(fragment, fragmentTag).commit()
    }
}