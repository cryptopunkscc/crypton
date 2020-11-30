package cc.cryptopunks.crypton.sys

import android.content.Context
import android.net.Uri
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.sys.file.getPath
import java.io.File


class AndroidUriSys(
    private val context: Context
) : URI.Sys {

    override fun resolve(uri: URI): File {
        return File(context.getPath(Uri.parse(uri.path))!!)
    }

//    override fun resolve(uri: URI): File {
//        val androidUri = Uri.parse(uri.path)
//        val name = androidUri.lastPathSegment!!
//        val extension = context.contentResolver.getType(androidUri)?.split("/")?.last()
//        val fullName = "$name.$extension"
//        val inputStream = context.contentResolver.openInputStream(androidUri)!!
//        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG)
//        ImageDecoder.createSource(context.contentResolver, androidUri)
//            .decodeBitmap { info, source -> }
//        context.getFileStreamPath(fullName).delete()
//        val writer = context.openFileOutput(fullName, Context.MODE_APPEND).bufferedWriter()
//        reader.copyTo(writer)
//        return context.getFileStreamPath(fullName)
//    }
}
