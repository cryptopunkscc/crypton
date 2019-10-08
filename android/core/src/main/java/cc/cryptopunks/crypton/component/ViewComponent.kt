package cc.cryptopunks.crypton.component

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.CoroutineScope

interface ViewComponent {
    val arguments: Bundle
    val view: View
    val viewScope: CoroutineScope
}