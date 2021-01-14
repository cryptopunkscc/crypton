package cc.cryptopunks.crypton.fs.util

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.io.File

fun File.recursiveFilesFlow(): Flow<File> = when {
    !exists() -> throw Exception()
    else -> channelFlow { sendRecursive(channel) }
}

private suspend fun File.sendRecursive(channel: SendChannel<File>) {
    if (!isDirectory) channel.send(this)
    else coroutineScope {
        launch {
            listFiles()?.forEach { child ->
                child.sendRecursive(channel)
            }
        }
    }
}
