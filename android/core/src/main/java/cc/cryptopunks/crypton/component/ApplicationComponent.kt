package cc.cryptopunks.crypton.component

import android.app.Activity
import android.app.Application

interface ApplicationComponent {
    val application: Application
    val mainActivityClass: Class<out Activity>
}