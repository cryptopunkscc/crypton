package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.activity.FeatureActivity
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.SessionScope

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

    fun SessionScope.fragmentScope() = SessionFragmentScope(this@FeatureFragment, this)
    fun ChatScope.fragmentScope() = ChatFragmentScope(this@FeatureFragment, this)
}


class SessionFragmentScope internal constructor(
    val fragment: Fragment,
    scope: SessionScope
) : SessionScope by scope

class ChatFragmentScope internal constructor(
    val fragment: Fragment,
    scope: ChatScope
) : ChatScope by scope
