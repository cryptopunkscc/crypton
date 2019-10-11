package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.model.Model

interface PresentationComponent :
    Client,
    ApplicationComponent,
    CoreComponent,
    NavigationComponent,
    ViewComponent {

    val modelScope: Model.Scope
}