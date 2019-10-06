package cc.cryptopunks.crypton.module

import android.app.Activity
import android.app.Application
import cc.cryptopunks.crypton.component.ClientComponent
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.component.RepoComponent
import cc.cryptopunks.crypton.BaseApplication
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.util.Scopes


class ApplicationModule(
    override val application: Application,
    override val mainActivityClass: Class<out Activity>,
    private val executorsComponent: ExecutorsComponent,
    private val repoComponent: RepoComponent,
    private val clientComponent: ClientComponent
) :
    BaseApplication.Component,
    ExecutorsComponent by executorsComponent,
    RepoComponent by repoComponent,
    ClientComponent by clientComponent,
    BroadcastError.Component by BroadcastError.Module() {

    override val useCaseScope = Scopes.UseCase(broadcastError)

    override fun featureComponent(): FeatureComponent = FeatureModule(this)
}
