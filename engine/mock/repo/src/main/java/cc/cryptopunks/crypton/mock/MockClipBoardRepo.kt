package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Clip

class MockClipBoardRepo : Clip.Board.Repo {
    override suspend fun put(clip: Clip) {
        TODO("Not yet implemented")
    }

    override suspend fun pop(): Clip? {
        TODO("Not yet implemented")
    }
}
