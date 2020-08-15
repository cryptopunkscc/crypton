package cc.cryptopunks.crypton.debug.drawer

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import io.palaima.debugdrawer.DebugDrawer
import io.palaima.debugdrawer.commons.BuildModule
import io.palaima.debugdrawer.commons.DeviceModule
import io.palaima.debugdrawer.commons.SettingsModule
import io.palaima.debugdrawer.scalpel.ScalpelModule

fun Application.initAppDebug() {

}

internal class DebugWrapper {
    lateinit var drawer: DebugDrawer

    fun closeDrawer() = drawer.closeDrawer()
    fun openDrawer() = drawer.openDrawer()
}

fun Activity.initDebugDrawer() = DebugWrapper().run {
    drawer = DebugDrawer
        .Builder(this@initDebugDrawer)
        .modules(
            BuildModule(),
            SettingsModule(),
            CryptonDebugModule(
                restart(),
                clearData(),
                debugView()
            ),
            DeviceModule(),
            ScalpelModule(this@initDebugDrawer)
        )
        .build()
}


fun Activity.detachDebugDrawer() {
    findViewById<ViewGroup>(android.R.id.content).apply {
//        findViewById<ViewGroup>(R.id.dd_drawer_layout)?.let { debugView ->
//            removeView(debugView)
//        } TODO
    }
}
