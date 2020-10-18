package cc.cryptopunks.crypton.fs.ormlite

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource


fun createConnectionSource(): ConnectionSource = JdbcConnectionSource(DATABASE_URL)

//private const val DATABASE_URL = "jdbc:h2:mem:lore"
private const val DATABASE_URL = "jdbc:h2:file:./test_storage/lore.h2"
