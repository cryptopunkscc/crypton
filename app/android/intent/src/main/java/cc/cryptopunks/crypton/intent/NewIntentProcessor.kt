package cc.cryptopunks.crypton.intent

import android.app.Activity
import androidx.fragment.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.util.logger.typedLog
import kotlinx.coroutines.runBlocking

class NewIntentProcessor(
    private val clipboardRepo: Clip.Board.Repo
) : (Intent) -> Unit {

    private val log = typedLog()

    override fun invoke(intent: Intent): Unit = intent.run {
        log.d { "processing intent: $intent" }
        when {
            intent.isCopyToClipboard() -> runBlocking {
                log.d { "inserting to clipboard" }
                clipboardRepo.put(
                    Clip(data = intent.getStringExtra(Intent.EXTRA_TEXT)!!)
                )
            }
        }
    }

    private fun Intent.isCopyToClipboard() =
        action == Intent.ACTION_SEND && type?.startsWith("text") ?: false
}


fun Fragment.showFileChooser() = getContentIntent().let { intent ->
    if (context?.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_ALL)?.size == 0)
        Toast.makeText(context, "No file manager present", Toast.LENGTH_LONG).show() else
        startActivityForResult(intent, FILE_CHOOSER_REQUEST)
}

fun getContentIntent() = Intent(Intent.ACTION_GET_CONTENT).apply {
    type = "file://*"
}

const val FILE_CHOOSER_REQUEST = 10001

fun Connectable.Binding.processActivityResult(
    requestCode: Int,
    resultCode: Int,
    intent: Intent?
) {
    when {
        resultCode != Activity.RESULT_OK -> Unit
        requestCode == FILE_CHOOSER_REQUEST -> intent?.data?.toString()?.let { path ->
            send(Exec.Upload(URI(path)))
        }
    }
}

