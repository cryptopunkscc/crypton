package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.dagger.DaggerBaseFragmentModule
import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.BaseFragment

abstract class ChatFragment : BaseFragment() {

    val component: ChatComponent by lazy {
        DaggerChatComponent.builder()
            .daggerFeatureModule(DaggerFeatureModule(baseActivity.featureComponent))
            .daggerBaseFragmentModule(DaggerBaseFragmentModule(this))
            .build()
    }

    private val address: Address get() = TODO()
}