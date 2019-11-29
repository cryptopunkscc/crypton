package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Clip

internal class ClipboardRepo : Clip.Board.Repo {

    private var clip: Clip? = null

    override suspend fun put(clip: Clip) {
        this.clip = clip
    }

    override suspend fun pop(): Clip? = clip
        .also { clip = null }
}