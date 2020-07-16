package cc.cryptopunks.crypton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cc.cryptopunks.crypton.debug.R
import io.palaima.debugdrawer.base.DebugModuleAdapter


internal fun setting(icon: Int, message: String, onClick: View.() -> Unit) =
    ViewBuilder(R.layout.setting) {
        findViewById<ImageView>(R.id.icon).setImageResource(icon)
        findViewById<TextView>(R.id.label).text = message
        setOnClickListener(onClick)
    }

internal data class ViewBuilder(
    val id: Int,
    val build: View.() -> Unit
)

internal class CryptonDebugModule(
    private vararg val builders: ViewBuilder
) : DebugModuleAdapter() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View = inflater.run {
        inflate(R.layout.container, parent, false).let { it as ViewGroup }.also { container ->
            builders.forEach { (id, build) ->
                container.addView(
                    inflater.inflate(id, container, false).apply(build)
                )
            }
        }
    }
}
