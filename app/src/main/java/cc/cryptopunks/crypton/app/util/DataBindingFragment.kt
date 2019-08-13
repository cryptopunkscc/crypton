package cc.cryptopunks.crypton.app.util

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.internal.disposables.CancellableDisposable

abstract class DataBindingFragment<VB : ViewDataBinding> :
    BaseFragment() {

    var binding: VB? = null
        private set

    open fun createBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): VB = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createBinding(
        layoutInflater = inflater,
        container = container
    ).also {
        binding = it
        viewDisposables.add(disposeBinding)
    }.root

    private val disposeBinding
        get() = CancellableDisposable {
            binding?.unbind()
            binding = null
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.onBindingCreated(
            binding = binding!!,
            savedInstanceState = savedInstanceState
        )
    }

    open fun Activity.onBindingCreated(
        binding: VB,
        savedInstanceState: Bundle?
    ) = Unit
}