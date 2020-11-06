package cc.cryptopunks.crypton.util.ormlite.jvm

import cc.cryptopunks.crypton.util.ormlite.ConnectionSourceFactory
import com.j256.ormlite.support.ConnectionSource

class JvmConnectionSourceFactory(
    private val prefix: String = "",
    private val path: String = "~/.crypton",
    private val inMemory: Boolean = false
) : ConnectionSourceFactory {
    override fun invoke(name: String): ConnectionSource {
        return createJdbcH2ConnectionSource(
            name = "$path/$prefix/$name",
            inMemory = inMemory
        )
    }
}
