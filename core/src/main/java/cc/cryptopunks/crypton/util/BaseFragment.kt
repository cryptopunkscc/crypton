package cc.cryptopunks.crypton.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController

abstract class BaseFragment : CoroutineFragment() {

    @get:LayoutRes
    open val layoutId
        get() = 0

    @get:StringRes
    open val titleId
        get() = 0

    open val navController get() = findNavController()

    val baseActivity get() = activity as BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutId.takeIf { it > 0 }?.let {
        inflater.inflate(it, container, false)
    }

    override fun onResume() {
        super.onResume()
        titleId.takeIf { it > 0 }?.let { id ->
            baseActivity.supportActionBar?.setTitle(id)
        }
    }
}