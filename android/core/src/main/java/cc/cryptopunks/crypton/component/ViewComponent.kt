package cc.cryptopunks.crypton.component

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.actor.Actor
import java.lang.ref.WeakReference

interface ViewComponent {
    val arguments: Bundle
    val view: View
    val weakView: WeakReference<View>
    val actorScope: Actor.Scope
}