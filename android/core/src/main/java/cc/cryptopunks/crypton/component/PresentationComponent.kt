package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.util.Scope

interface PresentationComponent :
    Client,
    ApplicationComponent,
    CoreComponent,
    NavigationComponent,
    ViewComponent {

    val presentationScope: Scope.Presentation
}