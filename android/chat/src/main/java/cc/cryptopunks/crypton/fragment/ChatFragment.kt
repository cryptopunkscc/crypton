package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatFragmentComponent
import cc.cryptopunks.crypton.util.invoke
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class ChatFragment : CoreFragment() {

    override val layoutRes: Int get() = R.layout.chat

    private val componentFlow: Flow<ChatComponent> by lazy {
        presentationComponentFlow.map { component ->
            DaggerChatFragmentComponent.builder()
                .presentationComponent(component).build()
                .createChatComponent()
        }
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        launch { componentFlow.collect(inject) }
    }

    private val inject: suspend ChatComponent.() -> Unit = {
        presentChat(chatView)
    }
}