package cc.cryptopunks.crypton.component

import android.os.Bundle
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation

interface PresentationComponent :
    Client,
    Core.Component,
    Navigation.Component,
    ApplicationComponent {

    val arguments: Bundle
}