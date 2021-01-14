package cc.cryptopunks.crypton.fs.parser.mp3

import cc.cryptopunks.crypton.fs.Id
import cc.cryptopunks.crypton.fs.Lore
import cc.cryptopunks.crypton.fs.util.idFromFile
import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.ID3v2
import com.mpatric.mp3agic.Mp3File
import java.io.File

const val TYPE_MP3 = "mp3"
const val TYPE_ID3V1 = "id3v1"
const val TYPE_ID3V2 = "id3v2"
const val TYPE_ID3_CUSTOM = "id3custom"

object Mp3LoreParser : Lore.Parse {
    override fun can(file: File): Boolean = file.extension == "mp3"
    override fun invoke(file: File): List<Lore> = file.mp3Lore()
}

fun File.mp3Lore(): List<Lore> =
    Mp3File(this).loreList(setOf(idFromFile(path)))

internal fun Mp3File.loreList(rel: Set<Id>): List<Lore> =
    mutableListOf<Lore>().apply {
        add(lore(rel))
        if (hasId3v1Tag()) add(id3v1Tag.lore(rel))
        if (hasId3v2Tag()) add(id3v2Tag.lore(rel))
        if (hasCustomTag()) add(customTag.lore(rel))
    }

internal fun Mp3File.lore(rel: Set<Id>) = Lore(
    type = TYPE_MP3,
    rel = rel,
    body = mapOfNotNull(
        "frameCount" to frameCount,
        "startOffset" to startOffset,
        "endOffset" to endOffset,
        "bitrate" to bitrate,
        "channelMode" to channelMode,
        "sampleRate" to sampleRate,
        "xingBitrate" to xingBitrate,
        "xingOffset" to xingOffset,
        "emphasis" to emphasis,
        "isCopyright" to isCopyright,
        "isOriginal" to isOriginal,
        "version" to version,
        "bitrates" to bitrates?.mapValues { (_, value) ->
            value.value
        },
    )
)

internal fun ID3v1.lore(rel: Set<Id>) = Lore(
    type = TYPE_ID3V1,
    rel = rel,
    body = mapOfNotNull(
        "album" to album,
        "track" to track,
        "artist" to artist,
        "comment" to comment,
        "genre" to genre,
        "genreDescription" to genreDescription,
        "year" to year,
        "title" to title,
        "version" to version
    )
)

internal fun ID3v2.lore(rel: Set<Id>) = Lore(
    type = TYPE_ID3V2,
    rel = rel,
    body = mapOfNotNull(
        "album" to album,
        "track" to track,
        "artist" to artist,
        "comment" to comment,
        "genre" to genre,
        "genreDescription" to genreDescription,
        "year" to year,
        "title" to title,
        "version" to version,
        "padding" to padding
    )
)

internal fun ByteArray.lore(rel: Set<Id>) = Lore(
    rel = rel,
    type = TYPE_ID3_CUSTOM,
    body = mapOf(
        "bytes" to this
    )
)


internal fun <K, V> mapOfNotNull(vararg pairs: Pair<K, V?>): Map<K, V> =
    mutableMapOf<K, V>().apply {
        pairs.forEach { (key, value) -> if (value != null) put(key, value) }
    }
