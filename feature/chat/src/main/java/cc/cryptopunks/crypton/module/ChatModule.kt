package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.entity.RemoteId
import dagger.Module
import dagger.Provides

@Module
class ChatModule(
    @get:Provides val remoteId: RemoteId
)