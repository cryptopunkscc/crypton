package cc.cryptopunks.crypton.module

import android.app.Activity
import android.app.Application
import cc.cryptopunks.crypton.BaseApplication
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.util.Scopes


class ApplicationModule(
    override val application: Application,
    override val mainActivityClass: Class<out Activity>,
    private val executorsComponent: ExecutorsComponent,
    private val repoComponent: Repo.Component,
    private val clientComponent: Client.Component
) :
    BaseApplication.Component,
    ExecutorsComponent by executorsComponent,
    Repo.Component by repoComponent,
    Client.Component by clientComponent,
    BroadcastError.Component by BroadcastError.Module() {

    override val useCaseScope = Scopes.UseCase(broadcastError)

    override fun featureComponent(): FeatureComponent = FeatureModule(this)
}
