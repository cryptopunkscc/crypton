package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatFragmentComponent
import cc.cryptopunks.crypton.util.invoke
import kotlinx.coroutines.*

class ChatFragment : CoreFragment() {

    override val layoutRes: Int get() = R.layout.chat

    private val component: Deferred<ChatComponent> = async(
        start = CoroutineStart.LAZY,
        context = Dispatchers.IO
    ) {
        DaggerChatFragmentComponent.builder()
            .presentationComponent(presentationComponent).build()
            .createChatComponent()
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        launch {
            component.await().run {
                presentChat(chatView)
            }
        }
    }
}