package cc.cryptopunks.crypton.navigate

import androidx.navigation.NavController
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.core.R

fun NavController.navigateCreateChat() =
    navigate(R.id.navigateCreateChat)

fun NavController.navigateChat(
    account: Address,
    chat: Address
) = navigate(
    R.id.navigateChat,
    bundle {
        it.account = account
        it.chat = chat
    }
)
