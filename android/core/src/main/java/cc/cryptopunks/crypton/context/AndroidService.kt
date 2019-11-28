package cc.cryptopunks.crypton.context

import android.app.Service

interface AndroidService {
    interface Core {
        interface Factory : (Service) -> Core {
            interface Core {
                val createServiceCore: Factory
            }
        }
    }
}