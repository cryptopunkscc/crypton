package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ContextMenu
import android.view.Gravity
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.iterator
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.author
import cc.cryptopunks.crypton.context.downloadFile
import cc.cryptopunks.crypton.context.getFile
import cc.cryptopunks.crypton.feature.OpenFile
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.android.synthetic.main.chat_message_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.launch
import java.io.File
import java.text.DateFormat
import kotlin.coroutines.CoroutineContext

class MessageView(
    context: Context,
    type: Int,
    override val coroutineContext: CoroutineContext,
    private val dateFormat: DateFormat,
    private val resolveUrlBody: ResolveUrlBody,
) : FrameLayout(
    if (type == Gravity.RIGHT) context
    else ContextThemeWrapper(
        context,
        R.style.Theme_Crypton_Colored
    )
), CoroutineScope {

    private val padding by lazy { resources.getDimensionPixelSize(R.dimen.message_padding) }

    val optionClicks = BroadcastChannel<Any>(1)

    var job: Job? = null

    var message: Message? = null
        set(message) {
            field = message?.updateView()
        }

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        inflate(R.layout.chat_message_item, true)
        setGravity(type)
        setOnLongClickListener { showContextMenu() }
        setOnClickListener {
            message?.run {
                if (this.type == Message.Type.Url)
                    optionClicks.offer(OpenFile(body))

            }
        }
    }

    private fun Message.updateView() = apply {
        when (type) {
            Message.Type.State -> {
                bodyTextView.text = "..."
                authorTextView.text = author
                statusTextView.text = null
                timestampTextView.text = null
                encryptedIcon.visibility = View.GONE
            }
            else -> {
                when (type) {
                    Message.Type.Info,
                    Message.Type.Text,
                    -> {
                        bodyTextView.text = body
                        bodyTextView.visibility = View.VISIBLE
                        bodyImageView.visibility = View.GONE
                    }
                    Message.Type.Url,
                    -> launch {
                        resolveUrlBody(body).let { resolved ->
                            when (resolved) {
                                is MessageBody.Image -> {
                                    bodyTextView.visibility = View.GONE
                                    bodyImageView.visibility = View.VISIBLE
                                    bodyImageView.setImageBitmap(resolved.bitmap)
                                }
                                is MessageBody.Data -> {
                                    // TODO handle unknown mime types
                                    bodyTextView.text = body
                                    bodyTextView.visibility = View.VISIBLE
                                    bodyImageView.visibility = View.GONE
                                }
                            }
                        }
                    }
                    else -> Unit
                }
                timestampTextView.text = dateFormat.format(timestamp)
                authorTextView.text = " $BULLET $author"
                statusTextView.text = StringBuffer(" $BULLET $status").apply {
                    if (encrypted) append(" $BULLET ")
                }
                encryptedIcon.visibility = if (encrypted) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu) {
        MenuInflater(context).inflate(R.menu.message, menu)
        menu.apply {
            setHeaderTitle(R.string.choose_option_label)
            iterator().forEach { item ->
                item.setOnMenuItemClickListener(onMenuItemCharSequence)
            }
            if (message?.type == Message.Type.Url) {
                findItem(R.id.openFile).isVisible = true
            }
        }
    }

    private val onMenuItemCharSequence = MenuItem.OnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.copyToClipboard -> Exec.Copy(message!!)
            R.id.delete -> Exec.DeleteMessage(message!!)
            R.id.openFile -> OpenFile(message!!.body)
            else -> null
        }?.let {
            optionClicks.offer(it)
        } == true
    }

    private fun setGravity(gravity: Int) = apply {
        when (gravity) {
            Gravity.LEFT -> setPadding(0, 0, padding, 0)
            Gravity.RIGHT -> setPadding(padding, 0, 0, 0)
            else -> setPadding(0, 0, 0, 0)
        }
        linearLayout.gravity = gravity
        cardContainer.gravity = gravity
    }

    private companion object {
        const val BULLET = '•'
    }
}

sealed class MessageBody {
    data class Image(val bitmap: Bitmap) : MessageBody()
    data class Data(val file: File) : MessageBody()
}

typealias ResolveUrlBody = suspend (String) -> MessageBody

fun RootScope.urlBodyResolver(): ResolveUrlBody = { url ->
    log.d { "resolving url: $url" }
    try {
        getFile(url).let { file ->
            log.d { "resolving url: ${file.path}" }
            if (!file.exists())
                downloadFile(url)

            when (file.extension) {
                "png", "jpg", "jpeg",
                -> MessageBody.Image(BitmapFactory.decodeFile(file.path))

                else
                -> MessageBody.Data(file)
            }
        }
    } catch (e: Throwable) {
        throw Exception(url, e)
    }
}
