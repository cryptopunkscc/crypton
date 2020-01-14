package cc.cryptopunks.crypton.activity

import android.view.MenuItem
import cc.cryptopunks.crypton.FeatureManager
import cc.cryptopunks.crypton.util.ext.bind
import cc.cryptopunks.crypton.util.ext.resolve
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

abstract class FeatureActivity : BaseActivity() {

    private val scope = MainScope()

    val key: Any get() = javaClass.name

    val featureManager
        get() = appCore
            .resolve<FeatureManager.Core>()
            .featureManager

    val featureCore by lazy { featureManager.request(key) }

    override fun onStart() {
        super.onStart()
        setSupportActionBar(toolbar)
        scope.launch { featureCore.navigationOutput.bind(navController) }
    }

    override fun onStop() {
        scope.coroutineContext.cancelChildren()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        scope.launch { featureCore.selectOptionItem(item.itemId) }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        scope.cancel()
        featureManager.release(key)
        super.onDestroy()
    }
}