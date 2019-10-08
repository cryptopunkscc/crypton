package cc.cryptopunks.crypton.component

import android.app.Activity
import android.app.Application

interface ApplicationComponent : CoreComponent {
    val application: Application
    val mainActivityClass: Class<out Activity>
}