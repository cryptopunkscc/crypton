package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.util.logger.typedLog

class MockClipBoardSys : Clip.Board.Sys {
    private val log = typedLog()
    override fun setClip(text: String) {
        log.d { "Set clip $text" }
    }
}
