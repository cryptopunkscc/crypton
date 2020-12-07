package cc.cryptopunks.crypton.context

typealias JavaFile = java.io.File

object File {

    interface Sys {
        fun imageSizeOf(image: JavaFile): Pair<Int, Int>
        fun filesDir(): JavaFile
        fun cacheDir(): JavaFile
        fun tmpDir(): JavaFile
    }
}
