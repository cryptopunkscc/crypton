package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RosterItemDecorator(
    context: Context,
    @DrawableRes resId: Int? = null,
    @Dimension private val paddingLeft: Int = 0,
    @Dimension private val paddingRight: Int = 0
) :
    RecyclerView.ItemDecoration() {

    private val divider: Drawable = if (resId == null)
        context.getDefaultDrawable() else
        context.getCustomDrawable(resId)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val left = parent.paddingLeft + paddingLeft
        val right = parent.width - parent.paddingRight - paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount - 1) {

            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    private companion object {

        val ATTRS = intArrayOf(android.R.attr.listDivider)

        fun Context.getCustomDrawable(@DrawableRes resId: Int) = ContextCompat
            .getDrawable(this, resId)
            ?: throw error("Cannot get custom drawable <id: $resId> for listDivider")

        fun Context.getDefaultDrawable() = this
            .obtainStyledAttributes(ATTRS)
            .run { getDrawable(0).also { recycle() } }
            ?: throw error("Cannot get default drawable for listDivider")
    }
}