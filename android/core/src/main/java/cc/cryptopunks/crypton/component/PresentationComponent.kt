package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.Scope

interface PresentationComponent :
    ApplicationComponent,
    CoreComponent,
    NavigationComponent,
    ViewComponent {

    val presentationScope: Scope.Presentation
}