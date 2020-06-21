package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.saveMessages
import cc.cryptopunks.crypton.interactor.sendMessages
import cc.cryptopunks.crypton.selector.fetchArchivedMessages
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal fun SessionScope.handleOmemoInitialized() =
    handle<Net.OmemoInitialized> {
        launch {
            log.d("handle ${this@handle}")
            launch {
                messageRepo.queuedListFlow().collect { messages ->
                    log.d("Sending queued $messages")
                    sendMessages(messages)
                }
            }
            launch {
                fetchArchivedMessages().collect { messages ->
                    log.d("Saving archived $messages")
                    saveMessages(messages).join()
                }
            }
        }
    }
