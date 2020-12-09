package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep
import java.io.File
import java.io.InputStream
import java.net.URI.create

val RootScope.uriSys: URI.Sys by dep()

data class URI(
    val path: String,
) {
    data class Data(
        val scheme: String = "",
        val authority: String = "",
        val path: String = "",
        val query: Map<String, String> = emptyMap(),
        val fragment: String = "",
    ) {
        override fun toString() = format()
    }

    interface Sys {
        fun resolve(uri: URI): File
        fun inputStream(uri: URI): InputStream
        fun getMimeType(uri: URI): String
    }
}

fun Map<String, String>.formatUriQuery(): String =
    toList().joinToString(";") { (key, value) -> "$key=$value" }

fun String.parseUriQuery(): Map<String, String> =
    split(";", "&")
        .map { it.split("=", limit = 2) }
        .map { it.first() to it.last() }
        .toMap()

val URI.Data.fileName get() = path.split("/").last()
val URI.Data.fileExtension get() = fileName.split(".", limit = 2).last()

fun String.parseUriData(): URI.Data =
    create(this).run {
        URI.Data(
            scheme = scheme ?: "",
            authority = authority ?: "",
            path = path ?: "",
            query = query
                ?.parseUriQuery()
                ?: emptyMap(),
            fragment = fragment ?: "",
        )
    }

fun URI.Data.format() = listOfNotNull(
    scheme.takeIf { it.isNotBlank() }?.let { "$it:" },
    authority.takeIf { it.isNotBlank() }?.let { "//$it" },
    path.takeIf { it.isNotBlank() }?.let { "/$it" },
    query.takeIf { it.isNotEmpty() }?.formatUriQuery()?.let { "?$it" },
    fragment.takeIf { it.isNotBlank() }?.let { "#$it" }
).joinToString("")
