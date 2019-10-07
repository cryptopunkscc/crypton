package cc.cryptopunks.crypton.module

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.component.ViewComponent
import cc.cryptopunks.crypton.fragment.CoreFragment
import kotlinx.coroutines.CoroutineScope

class ViewModule(
    override val arguments: Bundle,
    override val view: View,
    override val scope: CoroutineScope
) : ViewComponent

fun CoreFragment.viewComponent() = ViewModule(
    arguments = arguments ?: Bundle.EMPTY,
    view = view!!,
    scope = scope
)