package cc.cryptopunks.crypton.sys

import android.content.ClipData
import android.content.ClipboardManager
import cc.cryptopunks.crypton.context.Clip
import javax.inject.Inject

class SetToClipboard @Inject constructor(
    clipboard: ClipboardManager
) : Clip.Board.Sys.SetClip, (String) -> Unit by { text ->
    clipboard.setPrimaryClip(
        ClipData.newPlainText("", text)
    )
}