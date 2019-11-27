package cc.cryptopunks.crypton

import android.app.Service

interface ServiceCore {
    interface Factory : (Service) -> ServiceCore {
        interface Core {
            val createServiceCore: Factory
        }
    }
}