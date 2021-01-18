package cc.cryptopunks.crypton.fs.ormlite

import cc.cryptopunks.crypton.fsv2.Graph
import cc.cryptopunks.crypton.util.ormlite.createDao
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource

class OrmLiteGraph(
    connection: ConnectionSource,
) : Graph.ReadWrite {

    private val relations: Dao<Relation, String> = connection.createDao()
    private val data: Dao<DataType, String> = connection.createDao()

    override fun source(id: String): Set<String> = relations
        .queryForEq("sourceId", id)
        .asSequence()
        .map { it.targetId }
        .toSet()

    override fun target(id: String): Set<String> = relations
        .queryForEq("sourceId", id)
        .asSequence()
        .map { it.targetId }
        .toSet()

    override fun type(name: String): Set<String> = data
        .queryForEq("type", name)
        .asSequence()
        .map { it.id }
        .toSet()

    override fun stories(): Set<String> = data
        .queryForAll()
        .map { it.id }
        .toSet()

    override fun setRelation(target: String, source: String) {
        relations.createIfNotExists(Relation(sourceId = source, targetId = target))
    }

    override fun setType(id: String, type: String) {
        data.createIfNotExists(DataType(id, type))
    }

    override fun remove(id: String) {
        relations.deleteBuilder().where()
            .idEq(id)
            .query()
        data.deleteBuilder().where()
            .eq("sourceId", id).or()
            .eq("targetId", id)
            .query()
    }
}
