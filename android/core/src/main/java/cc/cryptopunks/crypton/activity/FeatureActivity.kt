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

    val key: Any = javaClass.name + System.currentTimeMillis()

    val featureManager
        get() = this
            .core.resolve<FeatureManager.Component>()
            .featureManager

    protected val feature by lazy { featureManager.request(key) }

    override fun onStart() {
        super.onStart()
        setSupportActionBar(toolbar)
        scope.launch { feature.navigateOutput.bind(navController) }
    }

    override fun onStop() {
        scope.coroutineContext.cancelChildren()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        scope.launch { feature.onOptionItemSelected(item.itemId) }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        scope.cancel()
        featureManager.release(key)
        super.onDestroy()
    }
}