package cc.cryptopunks.crypton.resolvers

import cc.cryptopunks.crypton.CannotResolve
import cc.cryptopunks.crypton.Context
import cc.cryptopunks.crypton.Resolve
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.chatRepo
import cc.cryptopunks.crypton.context.chatScope
import cc.cryptopunks.crypton.context.rootScope
import cc.cryptopunks.crypton.context.sessionScope

fun contextResolver(): Resolve = { context ->
    runCatching {
        resolveFromContext(context)
    }.getOrElse {
        Scoped.Resolved(this, CannotResolve(context))
    }
}

private suspend fun Scope.resolveFromContext(context: Any): Scoped.Resolved? =
    if (context !is Context) null
    else {
        when (this) {

            is ChatScope -> {
                when (context.id) {
                    chat.address.id -> Scoped.Resolved(this, context.next)
                    else -> sessionScope.resolveFromContext(context)
                }
            }

            is SessionScope -> {
                when {
                    context.id == account.address.id ->
                        when (val next = context.next) {
                            is Context -> resolveFromContext(next)
                            else -> Scoped.Resolved(this, next)
                        }

                    chatRepo.contains(address(context.id)) ->
                        chatScope(address(context.id)).resolveFromContext(context)

                    else ->
                        rootScope.resolveFromContext(context)
                }
            }

            is RootScope -> {
                sessionScope(address(context.id)).let { scope ->
                    when (val next = context.next) {
                        is Context -> scope.resolveFromContext(next)
                        else -> Scoped.Resolved(scope, next)
                    }
                }
            }

            else -> null
        }
    }
