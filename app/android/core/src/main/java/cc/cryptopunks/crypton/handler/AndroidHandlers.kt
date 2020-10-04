package cc.cryptopunks.crypton.handler

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.fragment.ChatFragmentScope
import cc.cryptopunks.crypton.handle
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun androidHandlers() = createHandlers {
    +handleSelect()
}

internal fun handleSelect() = handle { _, select: Exec.Select.Media ->
    this as ChatFragmentScope
    val activity = fragment.requireActivity()
    var chooser = false
    Intent().apply {
        when (select) {
            Exec.Select.Media.Image -> {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
                chooser = true
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            Exec.Select.Media.Video -> {
                action = MediaStore.ACTION_VIDEO_CAPTURE
            }
            Exec.Select.Media.Photo -> {
                val uri = newImageUri()
                action = MediaStore.ACTION_IMAGE_CAPTURE
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            Exec.Select.Media.File -> {
                action = Intent.ACTION_GET_CONTENT
                type = "*/*"
                chooser = true
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            Exec.Select.Media.Location -> {
                TODO()
            }
        }
    }
}

private fun newImageUri() = Environment
    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    .toString()
    .plus("/Camera/IMG_${IMAGE_DATE_FORMAT.format(Date())}.jpg")
    .let { File(it) }
    .apply { parentFile?.mkdir() }
    .let { Uri.fromFile(it) }

val IMAGE_DATE_FORMAT = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
