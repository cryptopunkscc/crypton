package cc.cryptopunks.crypton.sys

import android.content.ClipData
import android.content.ClipboardManager
import cc.cryptopunks.crypton.context.Clipboard
import javax.inject.Inject

class SetClip @Inject constructor(
    clipboard: ClipboardManager
) : Clipboard.Sys.SetClip, (String) -> Unit by { text ->
    clipboard.setPrimaryClip(
        ClipData.newPlainText("", text)
    )
}