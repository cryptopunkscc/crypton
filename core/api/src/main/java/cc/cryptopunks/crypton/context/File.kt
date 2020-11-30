package cc.cryptopunks.crypton.context

typealias JavaFile = java.io.File

object File {

    interface Sys {
        fun filesDir(): JavaFile
        fun cacheDir(): JavaFile
        fun tmpDir(): JavaFile
    }
}
