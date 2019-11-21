package cc.cryptopunks.crypton.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import cc.cryptopunks.crypton.util.ext.obtainString
import kotlinx.android.synthetic.main.action_input.view.*

class ActionInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        View.inflate(context, layout, this)
        context.obtainStyledAttributes(attrs, attrsArray).run {
            button.setImageDrawable(getDrawable(actionIconIndex))
            input.hint = obtainString(hintIndex)
            recycle()
        }
    }

    val input get() = inputEditText
    val button get() = actionButton

    private companion object {
        val layout get() = R.layout.action_input
        val attrsArray: IntArray get() = R.styleable.ActionInputView
        val actionIconIndex get() = R.styleable.ActionInputView_actionIcon
        val hintIndex get() = R.styleable.ActionInputView_hint
    }
}