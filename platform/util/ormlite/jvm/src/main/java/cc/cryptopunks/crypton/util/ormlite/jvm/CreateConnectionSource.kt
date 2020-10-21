package cc.cryptopunks.crypton.util.ormlite.jvm

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource

fun createJdbcH2ConnectionSource(
    name: String = "database",
    inMemory: Boolean = false,
): ConnectionSource = JdbcConnectionSource(buildH2Url(
    name = name,
    inMemory = inMemory
))

private fun buildH2Url(
    name: String = "",
    inMemory: Boolean = false,
) = "jdbc:h2:" + (if (inMemory) "mem" else "file") + ":" + name + ".h2"

