package cc.cryptopunks.crypton.component

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.actor.Actor

interface ViewComponent {
    val arguments: Bundle
    val view: View
    val actorScope: Actor.Scope
}