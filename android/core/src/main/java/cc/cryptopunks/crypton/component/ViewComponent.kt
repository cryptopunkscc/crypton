package cc.cryptopunks.crypton.component

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.util.Scope

interface ViewComponent {
    val arguments: Bundle
    val view: View
    val viewScope: Scope.View
}