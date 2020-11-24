package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.saveMessage
import cc.cryptopunks.crypton.selector.archivedMessagesFlow
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun saveMessages() = feature(

    emitter = emitter<SessionScope> {
        flowOf(
            netEvents().filterIsInstance<Net.OmemoInitialized>().flatMapConcat {
                archivedMessagesFlow().onEach {
                    log.d { "Archived messages" }
                }
            },
            incomingMessages().onEach {
                log.d { "Incoming message" }
            }.map {
                listOf(it.message)
            }
        ).flattenMerge().map {
            Exec.SaveMessages(it)
        }
    },

    handler = { _, (messages): Exec.SaveMessages ->
        messages.forEach { message ->
            when (message.status) {
                Message.Status.State -> when (message.text) {
                    Message.State.composing.name -> saveMessage(message.copy(id = message.simpleStatusId()))
                    Message.State.paused.name,
                    Message.State.active.name -> messageRepo.delete(id = message.simpleStatusId())
                    else -> Unit
                }
                else -> null
            } ?: saveMessage(message)
        }
    }
)

private fun Message.simpleStatusId() = "$chat:$from:$status"
