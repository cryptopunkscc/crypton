package cc.cryptopunks.crypton.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.util.logger.typedLog

abstract class LoggerFragment : Fragment() {

    val log = typedLog().builder

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        log.v { status = "onAttach" }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log.v { status = "onCreate" }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log.v { status = "onViewCreated" }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        log.v { status = "onStart" }
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        log.v { status = "onResume" }
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        log.v { status = "onPause" }
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        log.v { status = "onStop" }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        log.v { status = "onDestroy" }
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        log.v { status = "onDestroyView" }
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        log.v { status = "onDetach" }
    }

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}
