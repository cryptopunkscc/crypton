package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation

class PresentationModule(
    api: Core.Api,
    coreComponent: Core.Component,
    navigationComponent: Navigation.Component
) :
    PresentationComponent,
    Core.Api by api,
    Core.Component by coreComponent,
    Navigation.Component by navigationComponent