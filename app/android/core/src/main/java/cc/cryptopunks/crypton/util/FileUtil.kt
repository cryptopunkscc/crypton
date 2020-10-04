package cc.cryptopunks.crypton.util

import android.content.Context
import android.net.Uri
import java.io.File

fun Context.resolveFile(uri: Uri): File {
    val name = uri.lastPathSegment!!
    val extension = contentResolver.getType(uri)
    val fullName = "$name.$extension"
    val reader = contentResolver.openInputStream(uri)!!.reader()
    val writer = openFileOutput(fullName, Context.MODE_APPEND).writer()
    reader.copyTo(writer)
    return getFileStreamPath(fullName)
}
