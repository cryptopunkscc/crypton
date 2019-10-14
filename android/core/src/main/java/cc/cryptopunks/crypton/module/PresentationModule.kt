package cc.cryptopunks.crypton.module

import android.os.Bundle
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.applicationComponent
import cc.cryptopunks.crypton.component.ApplicationComponent
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.navigation.Navigation

class PresentationModule(
    client: Client,
    navigationComponent: Navigation.Component,
    override val arguments: Bundle = Bundle()
) :
    PresentationComponent,
    Client by client,
    ApplicationComponent by applicationComponent,
    Navigation.Component by navigationComponent