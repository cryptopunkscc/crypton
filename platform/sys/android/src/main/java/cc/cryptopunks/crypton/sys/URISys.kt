package cc.cryptopunks.crypton.sys

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.graphics.decodeBitmap
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.sys.file.getPath
import cc.cryptopunks.crypton.sys.file.getRealPath
import cc.cryptopunks.crypton.sys.file.getUriRealPath
import java.io.File


class URISys(
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
