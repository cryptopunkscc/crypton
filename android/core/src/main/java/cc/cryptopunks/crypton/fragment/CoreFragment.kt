package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import cc.cryptopunks.crypton.activity.CoreActivity
import cc.cryptopunks.crypton.module.PresentationFragmentModule


abstract class CoreFragment : CoroutineFragment() {

    open val layoutRes @LayoutRes get() = 0

    open val titleId @StringRes get() = 0

    val coreActivity get() = activity as CoreActivity

    val presentationComponent by lazy { PresentationFragmentModule(this) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutRes.takeIf { it > 0 }?.let {
        inflater.inflate(it, container, false)
    }

    override fun onResume() {
        super.onResume()
        titleId.takeIf { it > 0 }?.let { id ->
            coreActivity.supportActionBar?.setTitle(id)
        }
    }
}