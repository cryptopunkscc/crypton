@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service

class ServiceBinding {

    private var left: Service? = null
    private var right: Service? = null

    fun setLeft(left: Service?) {
        this.left?.destroy()
        this.left = left
        bind()
    }

    fun setRight(right: Service?): Unit = synchronized(this) {
        this.left?.stop()
        this.right?.destroy()
        this.right = right
        bind()
    }

    private fun bind() {
        right?.let { right -> left?.bind(right) }
    }

    fun snapshot() = Snapshot(left, right)

    class Snapshot internal constructor(
        val left: Service?,
        val right: Service?
    ) {
        val isVisible get() = left != null
        val isAttached get() = right != null
        inline fun <reified T : Any> isTypeOf() = left is T
    }
}