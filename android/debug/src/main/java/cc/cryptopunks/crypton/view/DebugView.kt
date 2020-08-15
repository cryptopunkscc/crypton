package cc.cryptopunks.crypton.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.activity.BaseActivity
import cc.cryptopunks.crypton.cli.context
import cc.cryptopunks.crypton.debug.R
import cc.cryptopunks.crypton.translator.Check
import cc.cryptopunks.crypton.translator.TranslationContext
import cc.cryptopunks.crypton.translator.translateCli
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.ScrollHelper
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.LogOutputCache
import cc.cryptopunks.crypton.widget.ActorLayout
import cc.cryptopunks.crypton.widget.autoAdjustActionButtons
import cc.cryptopunks.crypton.widget.autoAdjustPaddingOf
import kotlinx.android.synthetic.main.debug_view.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DebugView(
    context: BaseActivity
) : ActorLayout(context) {

    private val helper = ScrollHelper(context)

    private val debugAdapter = DebugAdapter()

    private var translationContext: TranslationContext = context()

    init {
        View.inflate(context, R.layout.debug_view, this)
        val recyclerView = debugRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = debugAdapter
        }
        messageInputView.apply {
            slash.visibility = View.GONE
            encrypt.visibility = View.GONE
            autoAdjustActionButtons()
            autoAdjustPaddingOf(recyclerView)
        }
    }

    override fun Connector.connect(): Job = launch {
        launch {
            debugAdapter.apply {
                items.addAll(LogOutputCache.Default)
                notifyDataSetChanged()
                helper.scrollToBottom(debugRecyclerView, smooth = false)
            }
            CoroutineLog.flow().collect { event ->
                debugAdapter += event
                delay(300)
                helper.scrollToBottom(debugRecyclerView)
            }
        }
        launch {
            messageInputView.input.textChanges().translateCli(translationContext).collect {
                translationContext = it
            }
        }
        launch {
            messageInputView.button.clicks().collect {
                when (val result = translationContext.result) {
                    is Check.Suggest -> debugAdapter += Log.Event(
                        label = "Debug",
                        message = result.toString(),
                        action = result
                    )
                    is Any -> result.out()
                }
            }
        }
    }
}

