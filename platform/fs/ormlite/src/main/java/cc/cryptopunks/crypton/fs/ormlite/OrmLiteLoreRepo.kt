package cc.cryptopunks.crypton.fs.ormlite

import cc.cryptopunks.crypton.fs.Lore
import cc.cryptopunks.crypton.util.ormlite.createDao
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource

class OrmLiteLoreRepo(
    private val connection: ConnectionSource,
) : Lore.Repo {

    private val loreDao: Dao<LoreData, String> = connection.createDao()
    private val relDao: Dao<RelData, String> = connection.createDao()

    override suspend fun plus(data: Lore): String =
        data.toRelDataList().run {
            val id = loreDao.createIfNotExists(first().target).id
            forEach { relData -> relDao.createIfNotExists(relData) }
            id
        }


    override fun get(id: String): Lore =
        loreDao.queryForId(id).toLore()

    override fun contains(id: String): Boolean =
        loreDao.idExists(id)

    override fun minus(id: String): Boolean =
        (loreDao.deleteById(id) == 1).also {
            relDao.deleteBuilder().where().eq("sourceId", id).query()
            relDao.deleteBuilder().where().eq("target", id).query()
        }

    override fun typeOf(any: Any): Boolean =
        any is Lore

    override fun refs(id: String): Set<Lore> = relDao
        .run { queryForEq("sourceId", id) }
        .asSequence()
        .map { it.target.toLore() }
        .toSet()

    override fun getByType(types: Set<String>): Set<Lore> = loreDao
        .queryBuilder()
        .where().`in`("type", types).query()
        .asSequence()
        .map { it.toLore() }
        .toSet()

    fun close() {
        connection.close()
    }
}
