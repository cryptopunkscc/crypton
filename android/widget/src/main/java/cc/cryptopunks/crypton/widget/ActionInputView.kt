package cc.cryptopunks.crypton.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import cc.cryptopunks.crypton.util.ext.obtainString
import kotlinx.android.synthetic.main.action_input.view.*
import kotlin.math.abs

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

    val slash get() = slashButton
    val encrypt get() = encryptToggle
    val input get() = inputEditText
    val button get() = actionButton
    val actionWrapper get() = actionButtonWrapper

    private companion object {
        val layout get() = R.layout.action_input
        val attrsArray: IntArray get() = R.styleable.ActionInputView
        val actionIconIndex get() = R.styleable.ActionInputView_actionIcon
        val hintIndex get() = R.styleable.ActionInputView_hint
    }
}


fun ActionInputView.autoAdjustActionButtons(
    orientationThreshold: Float = context.resources.displayMetrics.density * 48 * 2
) =
    addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        if (top != oldTop) actionWrapper.orientation =
            when (abs(top - bottom) > orientationThreshold) {
                true -> LinearLayout.VERTICAL
                false -> LinearLayout.HORIZONTAL
            }
    }

fun View.autoAdjustPaddingOf(
    adjustable: View
) =
    addOnLayoutChangeListener { _, _, top, _, _, _, _, _, _ ->
        adjustable.apply {
            setPadding(paddingLeft, paddingTop, paddingRight, this.bottom - top)
        }
    }

fun ActionInputView.setSlashClickListener() = slash.setOnClickListener {
    input.apply {
        val selection = selectionEnd
        if (text.firstOrNull() == '/') {
            setText(text.drop(1))
            setSelection(selection - 1)
        } else {
            setText("/$text")
            setSelection(selection + 1)
        }
    }
}
