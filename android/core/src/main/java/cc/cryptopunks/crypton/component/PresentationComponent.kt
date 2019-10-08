package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.Scopes

interface PresentationComponent :
    ApplicationComponent,
    CoreComponent,
    NavigationComponent,
    ViewComponent {

    val presentationScope: Scopes.Presentation
}