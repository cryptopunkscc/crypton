package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import cc.cryptopunks.crypton.activity.CoreActivity
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.applicationComponent
import cc.cryptopunks.crypton.module.PresentationFragmentModule
import kotlinx.coroutines.flow.map


abstract class CoreFragment : CoroutineFragment() {

    open val layoutRes @LayoutRes get() = 0

    open val titleId @StringRes get() = 0

    val coreActivity get() = activity as CoreActivity

    val presentationComponent by lazy {
        applicationComponent.currentClient().let { client ->
            createComponent(client)
        }
    }

    val presentationComponentFlow by lazy {
        applicationComponent.currentClient.map { client ->
            createComponent(client)
        }
    }

    private fun createComponent(client: Client) =
        PresentationFragmentModule(this, client)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutRes.takeIf { it > 0 }?.let {
        inflater.inflate(it, container, false)
    }

    override fun onResume() {
        super.onResume()
        titleId.takeIf { it > 0 }?.let { id ->
            coreActivity.supportActionBar?.setTitle(id)
        }
    }

    fun restart() {
        fragmentManager!!.beginTransaction()
            .detach(this)
            .attach(this)
            .commit()
    }
}