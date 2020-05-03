package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import cc.cryptopunks.crypton.activity.BaseActivity

abstract class BaseFragment : LoggerFragment() {

    open val layoutRes @LayoutRes get() = 0

    open val titleId @StringRes get() = 0

    private val baseActivity get() = activity as? BaseActivity

    fun setTitle(title: Any) {
        baseActivity?.supportActionBar?.apply {
            when (title) {
                is Int -> setTitle(title)
                is CharSequence -> setTitle(title)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutRes.takeIf { it > 0 }?.let {
        log.d("onCreateView")
        inflater.inflate(it, container, false)
    }

    override fun onResume() {
        super.onResume()
        titleId.takeIf { it > 0 }?.let { id ->
            baseActivity?.supportActionBar?.setTitle(id)
        }
    }
    fun restart() {
        fragmentManager!!.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
}
