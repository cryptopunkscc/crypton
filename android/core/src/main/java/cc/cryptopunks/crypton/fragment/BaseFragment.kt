package cc.cryptopunks.crypton.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.activity.BaseActivity


abstract class BaseFragment : LoggerFragment() {

    open val layoutRes @LayoutRes get() = 0

    open val titleId @StringRes get() = 0

    val baseActivity get() = activity as? BaseActivity

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
        log.d { "onCreateView" }
        inflater.inflate(it, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        titleId.takeIf { it > 0 }?.let { id ->
            baseActivity?.supportActionBar?.setTitle(id)
        }
    }

    fun restart() {
        parentFragmentManager.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
}


val Fragment.baseActivity get() = context as BaseActivity

val Fragment.rootScope get() = baseActivity.rootScope

private fun Activity.hideKeyboard() = window?.decorView?.windowToken.let { token ->
    getSystemService(Activity.INPUT_METHOD_SERVICE).let {
        it as InputMethodManager
    }.hideSoftInputFromWindow(token, 0)
}
