package cc.cryptopunks.crypton.resolvers

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Resolved
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.context.ChatScopeTag
import cc.cryptopunks.crypton.context.RootScopeTag
import cc.cryptopunks.crypton.context.SessionScopeTag
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.chatRepo
import cc.cryptopunks.crypton.context.createChatScope
import cc.cryptopunks.crypton.context.getSessionScope
import cc.cryptopunks.crypton.context.rootScope
import cc.cryptopunks.crypton.context.sessionScope
import cc.cryptopunks.crypton.create.resolver
import cc.cryptopunks.crypton.scopeTag
import kotlinx.coroutines.CoroutineScope

fun scopedResolver() = resolver<Scoped> {
    resolveFromScope(this, it) ?: Resolved(this, CannotResolve(it))
}

private class CannotResolve(val arg: Any) : Action

private suspend fun resolveFromScope(scope: CoroutineScope, context: Any): Action.Resolved? =
    if (context !is Scoped) null
    else {
        when (scope.scopeTag) {

            ChatScopeTag -> {
                when (context.id) {
                    scope.chat.address.id -> Resolved(scope, context.next)
                    else -> resolveFromScope(scope.sessionScope, context)
                }
            }

            SessionScopeTag -> {
                when {
                    context.id == scope.account.address.id ->
                        when (val next = context.next) {
                            is Scoped -> resolveFromScope(scope, next)
                            else -> Resolved(scope, next)
                        }

                    scope.chatRepo.contains(address(context.id)) ->
                        resolveFromScope(createChatScope(scope, address(context.id)), context)


                    else -> resolveFromScope(scope.rootScope, context)
                }
            }

            RootScopeTag -> {
                scope.getSessionScope(address(context.id)).let { scope ->
                    when (val next = context.next) {
                        is Scoped -> resolveFromScope(scope, next)
                        else -> Resolved(scope, next)
                    }
                }
            }

            else -> null
        }
    }
