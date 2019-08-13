package cc.cryptopunks.crypton.app.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cc.cryptopunks.crypton.app.module.FragmentModule
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment :
    Fragment() {

    @get:LayoutRes
    open val layoutId get() = 0

    val baseActivity get() = activity as BaseActivity

    val applicationComponent by lazy {
        baseActivity.applicationComponent
    }

    val fragmentComponent by lazy {
        applicationComponent
            .fragmentComponent()
            .plus(FragmentModule(this))
            .build()
    }

    val viewModelComponent by lazy {
        baseActivity
            .graphComponent
            .viewModelComponent()
            .build()
    }

    open val navController get() = findNavController()

    val viewDisposables = CompositeDisposable()
    val modelDisposables = CompositeDisposable()

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

    override fun onDestroyView() {
        viewDisposables.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        viewDisposables.dispose()
        modelDisposables.dispose()
        super.onDestroy()
    }
}