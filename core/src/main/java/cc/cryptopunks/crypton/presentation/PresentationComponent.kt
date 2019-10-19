package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation

interface PresentationComponent :
    Core.Api,
    Core.Component,
    Navigation.Component