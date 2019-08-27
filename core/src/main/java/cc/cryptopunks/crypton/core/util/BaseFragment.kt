package cc.cryptopunks.crypton.core.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cc.cryptopunks.crypton.core.component.DaggerFragmentComponent
import cc.cryptopunks.crypton.core.component.FragmentComponent

abstract class BaseFragment :
    Fragment() {

    @get:LayoutRes
    open val layoutId
        get() = 0

    @get:StringRes
    open val titleId
        get() = 0


    val baseActivity get() = activity as BaseActivity

    val applicationComponent by lazy {
        baseActivity.applicationComponent
    }

    val fragmentComponent: FragmentComponent by lazy {
        DaggerFragmentComponent
            .builder()
            .build()
    }

    open val navController get() = findNavController()

    val viewDisposable = ViewDisposable()
    val modelDisposable = ModelDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutId
        .takeIf { it != 0 }
        ?.let { inflater.inflate(it, container, false) }

    override fun onResume() {
        super.onResume()
        titleId.takeIf { it > 0 }?.let {
            baseActivity.supportActionBar?.setTitle(it)
        }
    }

    override fun onDestroyView() {
        viewDisposable.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewDisposable.dispose()
        modelDisposable.dispose()
        super.onDestroy()
    }
}