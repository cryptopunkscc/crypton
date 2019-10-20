package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.User
import kotlin.reflect.KClass

object Repo {

    interface Component {
        val accountRepo: Account.Repo
        val chatRepo: Chat.Repo
        val messageRepo: Message.Repo
        val userRepo: User.Repo
    }

    interface Provider {
        fun <T: Any> repo(type: KClass<T>): T
    }
}

inline fun <reified T: Any> Repo.Provider.repo() = repo(T::class)