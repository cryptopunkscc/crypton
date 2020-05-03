package cc.cryptopunks.crypton.intent

import android.content.Intent
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.runBlocking

class IntentProcessor(
    private val clipboardRepo: Clip.Board.Repo
) : (Intent) -> Unit {

    private val log = typedLog()

    override fun invoke(intent: Intent): Unit = intent.run {
        log.d("processing intent: $intent")
        when {
            intent.isCopyToClipboard() -> runBlocking {
                log.d("inserting to clipboard")
                clipboardRepo.put(
                    Clip(data = intent.getStringExtra(Intent.EXTRA_TEXT)!!)
                )
            }
        }
    }

    private fun Intent.isCopyToClipboard() =
        action == Intent.ACTION_SEND && type?.startsWith("text") ?: false

    interface Core {
        val processIntent: IntentProcessor
    }
}
