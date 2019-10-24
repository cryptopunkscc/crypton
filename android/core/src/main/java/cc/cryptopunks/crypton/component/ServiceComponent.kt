package cc.cryptopunks.crypton.component

import android.app.Service

interface ServiceComponent :
    ContextComponent,
    AndroidCore {
    val service: Service
}