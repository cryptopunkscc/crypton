package cc.cryptopunks.crypton.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.util.typedLog

abstract class LoggerFragment : Fragment() {

    val log = typedLog()

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        log.d("onAttach")
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log.d("onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log.d("onViewCreated")
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        log.d("onStart")
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        log.d("onResume")
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        log.d("onPause")
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        log.d("onStop")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        log.d("onDestroy")
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        log.d("onDestroyView")
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        log.d("onDetach")
    }

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}
