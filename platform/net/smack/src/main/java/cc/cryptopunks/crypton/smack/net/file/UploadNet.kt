package cc.cryptopunks.crypton.smack.net.file

import cc.cryptopunks.crypton.context.Upload
import cc.cryptopunks.crypton.smack.SmackCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.io.File

internal class UploadNet(
    core: SmackCore
) : SmackCore by core,
    Upload.Net {

    override fun upload(file: File): Flow<Upload.Progress> = channelFlow {
        launch(Dispatchers.IO) {
            val name = file.name
            val url = httpFileUploadManager.uploadFile(file) { uploaded, total ->
                offer(Upload.Progress(name, uploaded, total))
            }
            val size = file.length()
            send(Upload.Progress(name, size, size, url))
            close()
        }
    }
}
