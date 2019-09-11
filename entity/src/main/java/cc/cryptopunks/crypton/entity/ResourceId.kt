package cc.cryptopunks.crypton.entity

import androidx.room.Embedded
import androidx.room.PrimaryKey

data class ResourceId(
    @Embedded @PrimaryKey val remoteId: RemoteId = RemoteId.Empty,
    val resource: String = ""
) : CharSequence by buildString({
    append(remoteId)
    if (resource.isNotEmpty()) {
        append("/")
        append(resource)
    }
}) {

    val withoutResource get() = remoteId

    override fun toString(): String = substring(0, length)

    companion object {
        val Empty = ResourceId()

        fun from(string: String) = string.split("/").run {
            ResourceId(
                remoteId = RemoteId.from(get(0)),
                resource = get(1)
            )
        }
    }
}