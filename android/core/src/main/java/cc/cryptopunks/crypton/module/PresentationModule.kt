package cc.cryptopunks.crypton.module

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.activity.CoreActivity
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.applicationComponent
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.component.NavigationComponent
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.fragment.CoreFragment
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.CoroutineScope

abstract class PresentationModule(
    activity: CoreActivity,
    client: Client
) :
    PresentationComponent,
    Client by client,
    ApplicationComponent by applicationComponent,
    NavigationComponent by activity.navigationComponent {

    override val presentationScope = Scope.Presentation(broadcastError)
}

class PresenationActivityModule(
    private val activity: CoreActivity,
    client: Client
) :
    PresentationModule(activity, client) {

    override val arguments: Bundle get() = activity.intent.extras!!
    override val viewScope: CoroutineScope = activity
    override val view: View
        get() = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
}

class PresentationFragmentModule(
    private val fragment: CoreFragment,
    client: Client
) :
    PresentationModule(fragment.coreActivity, client) {

    override val arguments: Bundle get() = fragment.arguments!!
    override val viewScope: CoroutineScope get() = fragment
    override val view: View get() = fragment.view!!
}