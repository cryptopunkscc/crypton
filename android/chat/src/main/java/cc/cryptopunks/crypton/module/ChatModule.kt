package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.entity.Chat
import dagger.Module
import dagger.Provides

@Module
data class ChatModule(
    @get:Provides val chat: Chat
)