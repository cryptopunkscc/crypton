package cc.cryptopunks.crypton.component

import android.app.Service

interface ServiceComponent : ContextComponent {
    val service: Service
}