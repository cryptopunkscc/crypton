package cc.cryptopunks.crypton.fragment

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.util.typedLog

abstract class LoggerFragment : Fragment() {

    private val log = typedLog()

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
    override fun onDetach() {
        super.onDetach()
        log.d("onDetach")
    }

}