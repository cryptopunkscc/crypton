package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.activity.CoreActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext


abstract class CoreFragment : Fragment(), CoroutineScope {

    open val layoutRes @LayoutRes get() = 0

    open val titleId @StringRes get() = 0

    val scope = MainScope()

    override val coroutineContext: CoroutineContext get() = scope.coroutineContext

    val coreActivity get() = activity as CoreActivity

    val navigation get() = coreActivity.navigationComponent

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

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    fun restart() {
        fragmentManager!!.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
}