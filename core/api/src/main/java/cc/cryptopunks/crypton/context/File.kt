package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep

typealias JavaFile = java.io.File

val RootScope.fileSys: File.Sys by dep()

object File {

    interface Sys {
        fun imageSizeOf(image: JavaFile): Pair<Int, Int>
        fun filesDir(): JavaFile
        fun cacheDir(): JavaFile
        fun tmpDir(): JavaFile
    }
}
