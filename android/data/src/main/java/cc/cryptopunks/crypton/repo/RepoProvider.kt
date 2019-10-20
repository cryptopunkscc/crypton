package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.User
import kotlin.reflect.KClass

class RepoProvider(
    component: Repo.Component
) :
    Repo.Provider,
    Repo.Component by component {

    override fun <T : Any> repo(type: KClass<T>): T = when (type) {
        Message.Repo::class -> messageRepo
        Chat.Repo::class -> chatRepo
        Account.Repo::class -> accountRepo
        User.Repo::class -> userRepo
        else -> throw Exception("Cannot provide repo of key: $type")
    } as T

}