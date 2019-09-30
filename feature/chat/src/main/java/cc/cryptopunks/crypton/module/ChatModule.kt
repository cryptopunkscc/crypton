package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.entity.Address
import dagger.Module
import dagger.Provides

@Module
class ChatModule(
    @get:Provides val address: Address
)