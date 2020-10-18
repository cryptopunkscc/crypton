package cc.cryptopunks.crypton.fs.ormlite

import cc.cryptopunks.crypton.fs.Lore
import cc.cryptopunks.crypton.fs.util.calculateId
import cc.cryptopunks.crypton.fs.util.formatYaml
import cc.cryptopunks.crypton.fs.util.parseYaml
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable


@DatabaseTable(tableName = "lore")
internal data class LoreData(
    @DatabaseField(id = true)
    val id: String = "",
    @DatabaseField
    val ver: Int = 0,
    @DatabaseField(index = true)
    val type: String = "",
    @DatabaseField(dataType = DataType.LONG_STRING)
    val body: String = ""
) {
    companion object {
        val Empty = LoreData()
    }
}

@DatabaseTable(tableName = "rel")
internal data class RelData(
    @DatabaseField(id = true)
    val id: String = "",
    @DatabaseField(index = true)
    val sourceId: String = "",
    @DatabaseField(foreign = true)
    val target: LoreData = LoreData.Empty,
)

internal fun Lore.toRelDataList(
    id: String = calculateId()
): List<RelData> = LoreData(
    id = id,
    ver = ver,
    type = type,
    body = body.formatYaml()
).let { loreData ->
    rel.map { relId ->
        RelData(
            id = (relId + loreData.id).calculateId(),
            sourceId = relId,
            target = loreData
        )
    }
}

internal fun LoreData.toLore() = Lore(
    ver = ver,
    type = type,
    body = body.parseYaml()
)
