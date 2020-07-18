package cc.cryptopunks.crypton

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import io.palaima.debugdrawer.DebugDrawer
import io.palaima.debugdrawer.commons.BuildModule
import io.palaima.debugdrawer.commons.DeviceModule
import io.palaima.debugdrawer.commons.SettingsModule
import io.palaima.debugdrawer.scalpel.ScalpelModule
import io.palaima.debugdrawer.timber.TimberModule
import io.palaima.debugdrawer.timber.data.LumberYard
import timber.log.Timber

fun Application.initAppDebug() {

    val lumberYard = LumberYard.getInstance(this)
    lumberYard.cleanUp()

    Timber.plant(lumberYard.tree())
}

fun Activity.initDebugDrawer(): DebugDrawer = DebugDrawer
    .Builder(this)
    .modules(
        BuildModule(),
        SettingsModule(),
        CryptonDebugModule(
            restart(),
            clearData()
        ),
        DeviceModule(),
        TimberModule(),
        ScalpelModule(this)
    )
    .build()


fun Activity.detachDebugDrawer() {
    findViewById<ViewGroup>(android.R.id.content).apply {
//        findViewById<ViewGroup>(R.id.dd_drawer_layout)?.let { debugView ->
//            removeView(debugView)
//        } TODO
    }
}
