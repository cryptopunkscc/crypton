package cc.cryptopunks.crypton.sys

import android.content.ClipData
import android.content.ClipboardManager
import cc.cryptopunks.crypton.context.Clip

internal class ClipBoardSys(
    private val clipboard: ClipboardManager
) : Clip.Board.Sys {
    override fun setClip(text: String) = clipboard.setPrimaryClip(
        ClipData.newPlainText("", text)
    )
}
