package cc.cryptopunks.crypton.module

import android.app.Activity
import android.app.Application
import cc.cryptopunks.crypton.component.ApplicationComponent

class ApplicationModule(
    override val application: Application,
    override val mainActivityClass: Class<out Activity>
) :
    ApplicationComponent

