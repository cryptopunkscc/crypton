package cc.cryptopunks.crypton.feature

import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.context.applicationId
import cc.cryptopunks.crypton.context.getFile
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.fragment.fragment


data class OpenFile(val uri: String) : Action

fun openFile() = feature(

    handler {_, (uri): OpenFile ->
        val fragment = fragment
        val file = getFile(uri)
//        val intent = Intent().apply {
//            action = Intent.ACTION_VIEW
//            setDataAndType(
//                Uri.fromFile(file),
//                MimeTypeMap.getSingleton()
//                    .getMimeTypeFromExtension(file.extension)
//            )
//        }


        val intent = Intent(Intent.ACTION_VIEW).apply {
            val data: Uri = FileProvider.getUriForFile(
                fragment.requireContext(),
                "${applicationId.value}.provider",
                file
            )
            setData(data)
//            setDataAndType(data, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        fragment.startActivity(intent)
    }
)
