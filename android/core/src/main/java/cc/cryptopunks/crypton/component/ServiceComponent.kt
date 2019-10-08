package cc.cryptopunks.crypton.component

import android.app.Service

interface ServiceComponent :
    ContextComponent,
    ApplicationComponent {
    val service: Service
}