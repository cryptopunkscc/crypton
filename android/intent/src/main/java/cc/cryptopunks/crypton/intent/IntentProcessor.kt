package cc.cryptopunks.crypton.intent

import android.content.Intent
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.typedLog
import javax.inject.Inject

class IntentProcessor @Inject constructor(
    private val scope: Service.Scope,
    private val clipboardRepo: Clip.Board.Repo
) : (Intent) -> Unit {

    private val log = typedLog()

    override fun invoke(intent: Intent): Unit = intent.run {
        log.d("processing intent: $intent")
        when (action) {
            Intent.ACTION_SEND -> when {
                type?.startsWith("text/") == true -> scope.launch {
                    log.d("inserting to clipboard")
                    clipboardRepo.put(
                        Clip(data = intent.getStringExtra(Intent.EXTRA_TEXT)!!)
                    )
                }
            }
        }
    }

    interface Core {
        val processIntent: IntentProcessor
    }
}