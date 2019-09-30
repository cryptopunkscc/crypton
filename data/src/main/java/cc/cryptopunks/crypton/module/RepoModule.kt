package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.repo.AccountRepo
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.repo.UserRepo
import dagger.Binds
import dagger.Module

object RepoModule {

    @Module
    interface Bindings {

        @Binds
        fun account(repo: AccountRepo) : Account.Repo

        @Binds
        fun chat(repo: ChatRepo) : Chat.Repo

        @Binds
        fun message(repo: MessageRepo) : Message.Repo

        @Binds
        fun user(repo: UserRepo) : User.Repo
    }
}