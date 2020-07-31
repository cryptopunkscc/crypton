package cc.cryptopunks.crypton.util

interface LogCompanion {
    suspend fun output(output: Log.Output)
}
