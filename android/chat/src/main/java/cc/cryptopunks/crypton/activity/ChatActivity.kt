package cc.cryptopunks.crypton.activity

import android.os.Bundle
import cc.cryptopunks.crypton.chat.R

class ChatActivity : CoreActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_frame)
        navController.apply { setGraph(graph, intent.extras) }
    }
}