package cc.cryptopunks.crypton.format

import cc.cryptopunks.crypton.context.Roster
import java.util.*

internal fun Roster.Items.format() =
    list.joinToString("\n", transform = Roster.Item::format)

internal fun Roster.Item.format() =
    "${Date(updatedAt)} ($unreadMessagesCount) $account - $chatAddress: ${message.body}"
