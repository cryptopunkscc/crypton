package cc.cryptopunks.crypton.sys.file

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore


/* Get uri related content real local file path. */
fun Context.getUriRealPath(
    uri: Uri
): String = getUriRealPathAboveKitkat(this, uri)


private fun getUriRealPathAboveKitkat(
    ctx: Context,
    uri: Uri
): String {
    val contentResolver = ctx.contentResolver
    var ret: String? = ""
    if (isContentUri(uri)) {
        ret = (if (isGooglePhotoDoc(uri.authority))
            uri.lastPathSegment else
            getImageRealPath(contentResolver, uri, null))
    } else if (isFileUri(uri)) {
        ret = uri.path
    } else if (isDocumentUri(ctx, uri)) {

        // Get uri related document id.
        val documentId = DocumentsContract.getDocumentId(uri)

        // Get uri authority.
        val uriAuthority = uri.authority
        if (isMediaDoc(uriAuthority)) {
            val idArr = documentId.split(":".toRegex()).toTypedArray()
            if (idArr.size == 2) {
                // First item is document type.
                val docType = idArr[0]

                // Second item is document real id.
                val realDocId = idArr[1]

                // Get content uri by document type.
                val mediaContentUri = when (docType) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

                // Get where clause with real document id.
                val whereClause = MediaStore.Images.Media._ID + " = " + realDocId
                ret = getImageRealPath(contentResolver, mediaContentUri, whereClause)
            }
        } else if (isDownloadDoc(uriAuthority)) {
            // Build download uri.
            val downloadUri =
                Uri.parse("content://downloads/public_downloads")

            // Append download document id at uri end.
            val downloadUriAppendId =
                ContentUris.withAppendedId(downloadUri, java.lang.Long.valueOf(documentId))
            ret = getImageRealPath(contentResolver, downloadUriAppendId, null)
        } else if (isExternalStoreDoc(uriAuthority)) {
            val idArr =
                documentId.split(":".toRegex()).toTypedArray()
            if (idArr.size == 2) {
                val type = idArr[0]
                val realDocId = idArr[1]
                if ("primary".equals(type, ignoreCase = true)) {
                    ret = ctx.getExternalFilesDir(null)!!.absolutePath + "/" + realDocId
//                    ret = Environment.getExternalStorageDirectory()
//                        .toString() + "/" + realDocId
                }
            }
        }
    }
    return ret ?: ""
}

/* Check whether this uri represent a document or not. */
private fun isDocumentUri(
    ctx: Context,
    uri: Uri
): Boolean = DocumentsContract.isDocumentUri(ctx, uri)

/* Check whether this uri is a content uri or not.
*  content uri like content://media/external/images/media/1302716
*  */
private fun isContentUri(uri: Uri): Boolean = "content".equals(uri.scheme, ignoreCase = true)

/* Check whether this uri is a file uri or not.
*  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
* */
private fun isFileUri(uri: Uri): Boolean = "file".equals(uri.scheme, ignoreCase = true)


/* Check whether this document is provided by ExternalStorageProvider. */
private fun isExternalStoreDoc(uriAuthority: String?): Boolean =
    "com.android.externalstorage.documents" == uriAuthority

/* Check whether this document is provided by DownloadsProvider. */
private fun isDownloadDoc(uriAuthority: String?): Boolean =
    "com.android.providers.downloads.documents" == uriAuthority

/* Check whether this document is provided by MediaProvider. */
private fun isMediaDoc(uriAuthority: String?): Boolean =
    "com.android.providers.media.documents" == uriAuthority

/* Check whether this document is provided by google photos. */
private fun isGooglePhotoDoc(uriAuthority: String?): Boolean =
    "com.google.android.apps.photos.content" == uriAuthority

/* Return uri represented document file real local path.*/
private fun getImageRealPath(
    contentResolver: ContentResolver,
    uri: Uri,
    whereClause: String?
): String {
    var ret = ""

    // Query the uri with condition.
    val cursor: Cursor? = contentResolver.query(uri, null, whereClause, null, null)
    if (cursor != null) {
        val moveToFirst: Boolean = cursor.moveToFirst()
        if (moveToFirst) {

            // Get columns name by uri type.
            var columnName = MediaStore.Images.Media.DATA
            if (uri === MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Images.Media.DATA
            } else if (uri === MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Audio.Media.DATA
            } else if (uri === MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                columnName = MediaStore.Video.Media.DATA
            }

            // Get column index.
            val imageColumnIndex: Int = cursor.getColumnIndex(columnName)

            // Get column value which is the uri related file local path.
            ret = cursor.getString(imageColumnIndex)
        }
    }
    return ret
}

fun Context.getRealPath(_uri: Uri): String? =
    if ("content" == _uri.scheme) contentResolver.query(
        _uri,
        arrayOf(MediaStore.Images.ImageColumns.DATA),
        null,
        null,
        null
    )!!.run {
        moveToFirst()
        getString(0).also { close() }
    }
    else _uri.path

fun Context.getPath(uri: Uri): String? {
    val isKitKat: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
        println("getPath() uri: $uri")
        println("getPath() uri authority: " + uri.authority)
        println("getPath() uri path: " + uri.path)

        // ExternalStorageProvider
        if ("com.android.externalstorage.documents" == uri.authority) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            println("getPath() docId: " + docId + ", split: " + split.size + ", type: " + type)

            // This is for checking Main Memory
            return if ("primary".equals(type, ignoreCase = true)) {
                if (split.size > 1) {
                    Environment.getExternalStorageDirectory()
                        .toString() + "/" + split[1] + "/"
                } else {
                    Environment.getExternalStorageDirectory().toString() + "/"
                }
                // This is for checking SD Card
            } else {
                "storage" + "/" + docId.replace(":", "/")
            }
        }
    }
    return null
}
