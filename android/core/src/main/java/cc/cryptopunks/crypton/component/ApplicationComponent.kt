package cc.cryptopunks.crypton.component

import android.app.Activity
import android.app.Application
import cc.cryptopunks.crypton.core.Core

interface ApplicationComponent : Core.Component {
    val application: Application
    val mainActivityClass: Class<out Activity>
}