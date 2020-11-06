package cc.cryptopunks.crypton.util.ormlite.jvm

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource

fun createJdbcH2ConnectionSource(
    home: String = "./",
    name: String = "database",
    inMemory: Boolean = false,
): ConnectionSource = JdbcConnectionSource(buildH2Url(
    home = home,
    name = name,
    inMemory = inMemory
))

private fun buildH2Url(
    home: String,
    name: String,
    inMemory: Boolean = false,
) = "jdbc:h2:" + (if (inMemory) "mem" else "file") + ":" + (if (inMemory) name else home + name) + ".h2"

