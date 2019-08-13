package cc.cryptopunks.crypton.app.util

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.internal.disposables.CancellableDisposable

abstract class DataBindingActivity<VB : ViewDataBinding> :
    BaseActivity() {

    open val layoutId @LayoutRes get() = 0

    open val binding: VB by lazy {
        DataBindingUtil.setContentView<VB>(this, layoutId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        disposable.add(disposeBinding)
    }

    private val disposeBinding
        get() = CancellableDisposable {
            binding.unbind()
        }

}