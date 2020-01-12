@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Job

class ServiceBinding {

    private val binding = TwoWayBinding()

    private var leftJob: Job = Job()

    var left: Service? = null
        set(value) {
            leftJob.cancel()
            field = value?.apply {
                leftJob = binding.left.bind()
            }
        }

    private var rightJob: Job = Job()

    var right: Service? = null
        set(value) {
            rightJob.cancel()
            field = value?.apply {
                rightJob = binding.right.bind()
            }
        }

    fun clear() {
        left = null
        right = null
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