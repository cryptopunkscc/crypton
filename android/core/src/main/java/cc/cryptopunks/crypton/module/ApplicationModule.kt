package cc.cryptopunks.crypton.module

import android.app.Activity
import android.app.Application
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.component.CoreComponent

class ApplicationModule(
    override val application: Application,
    override val mainActivityClass: Class<out Activity>,
    coreModule: CoreModule
) :
    ApplicationComponent,
    CoreComponent by coreModule

