package cc.cryptopunks.crypton.module

import android.app.Service
import cc.cryptopunks.crypton.component.ContextComponent
import cc.cryptopunks.crypton.component.ServiceComponent

class ServiceModule private constructor(
    contextComponent: ContextComponent
) : ServiceComponent,
    ContextComponent by contextComponent {

    constructor(
        context: Service
    ) : this(ContextModule(context))
}

fun Service.serviceComponent() = ServiceModule(this)