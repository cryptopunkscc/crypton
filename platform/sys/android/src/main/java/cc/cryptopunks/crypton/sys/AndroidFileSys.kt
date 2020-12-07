package cc.cryptopunks.crypton.sys

import android.content.Context
import android.graphics.BitmapFactory
import cc.cryptopunks.crypton.context.File
import cc.cryptopunks.crypton.context.JavaFile

class AndroidFileSys(
    val context: Context,
) : File.Sys {
    override fun imageSizeOf(image: JavaFile): Pair<Int, Int> =
        BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(image.absolutePath, this)
            outWidth to outHeight
        }

    override fun filesDir(): JavaFile = context.filesDir
    override fun cacheDir(): JavaFile = context.cacheDir
    override fun tmpDir(): JavaFile = context.cacheDir.resolve("tmp").apply { mkdir() }
}
