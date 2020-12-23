package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.activity.FeatureActivity
import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.dep
import kotlinx.coroutines.CoroutineScope

abstract class FeatureFragment :
    CoroutineFragment() {

    private val featureActivity get() = activity as FeatureActivity
    private val key by lazy { featureActivity.key }

    val rootScope get() = featureActivity.rootScope

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun SessionScope.fragmentScope() = SessionFragmentModule(this@FeatureFragment, this)
    fun ChatScope.fragmentScope() = ChatScope.Module(coroutineContext + this@FeatureFragment.asDep<Fragment>())
}

val CoroutineScope.fragment: Fragment by dep()

interface FragmentScope : Scope {
    val fragment: Fragment
}

class SessionFragmentModule internal constructor(
    override val fragment: Fragment,
    scope: SessionScope
) : SessionScope by scope,
    FragmentScope

class ChatFragmentModule internal constructor(
    override val fragment: Fragment,
    scope: ChatScope
) : ChatScope by scope,
    FragmentScope
