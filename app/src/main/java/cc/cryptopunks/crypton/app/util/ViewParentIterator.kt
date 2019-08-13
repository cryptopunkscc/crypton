package cc.cryptopunks.crypton.app.util

import android.view.View

class ViewParentIterator(
    view: View
) : Iterator<View> {

    private var next: View? = view

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): View {
        return next!!.apply {
            next = parent as? View
        }
    }
}