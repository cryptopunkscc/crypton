package cc.cryptopunks.crypton.util

import android.content.Context
import androidx.core.content.ContextCompat
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.core.R

val presenceStatusColorIds = mapOf(
    Presence.Status.Unavailable to R.color.presence_unavailable,
    Presence.Status.Available to R.color.presence_available,
    Presence.Status.Error to R.color.presence_error
)

fun Context.presenceStatusColors() = let { context ->
    val default = ContextCompat.getColor(context, R.color.presence_error)
    presenceStatusColorIds
        .mapValues { (_, id) -> ContextCompat.getColor(context, id) }
        .withDefault { default }
}