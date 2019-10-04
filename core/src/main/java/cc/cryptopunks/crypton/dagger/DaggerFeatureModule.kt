package cc.cryptopunks.crypton.dagger

import android.app.Application
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.crypton.util.Scopes
import dagger.Module
import dagger.Provides

@Module
class DaggerFeatureModule(
    private val component: FeatureComponent
) : FeatureComponent {

    @get:Provides
    override val application: Application
        get() = component.application
    @get:Provides
    override val useCaseScope: Scopes.UseCase
        get() = component.useCaseScope
    @get:Provides
    override val accountRepo: Account.Repo
        get() = component.accountRepo
    @get:Provides
    override val chatRepo: Chat.Repo
        get() = component.chatRepo
    @get:Provides
    override val messageRepo: Message.Repo
        get() = component.messageRepo
    @get:Provides
    override val userRepo: User.Repo
        get() = component.userRepo
    @get:Provides
    override val createClient: Client.Factory
        get() = component.createClient
    @get:Provides
    override val clientCache: Client.Cache
        get() = component.clientCache
    @get:Provides
    override val onOptionItemSelected: OptionItemSelected.Input
        get() = component.onOptionItemSelected
    @get:Provides
    override val optionItemSelections: OptionItemSelected.Output
        get() = component.optionItemSelections
    @get:Provides
    override val navigate: Navigate
        get() = component.navigate
    @get:Provides
    override val navigateOutput: Navigate.Output
        get() = component.navigateOutput
    @get:Provides
    override val broadcastError: BroadcastError
        get() = component.broadcastError

    @Provides
    override fun featureComponent(): FeatureComponent =
        component.featureComponent()
}