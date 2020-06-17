package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Clip

class ClipBoardRepo : Clip.Board.Repo {
    override suspend fun put(clip: Clip) {
        TODO("Not yet implemented")
    }

    override suspend fun pop(): Clip? {
        TODO("Not yet implemented")
    }
}
