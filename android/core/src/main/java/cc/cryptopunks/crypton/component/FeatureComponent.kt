package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.BaseApplication
import cc.cryptopunks.crypton.activity.CoreActivity
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.fragment.DependenciesFragment
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.crypton.util.ext.fragment

interface FeatureComponent :
    BaseApplication.Component,
    OptionItemSelected.Component,
    Navigate.Component

fun CoreActivity.featureComponent(): FeatureComponent = fragment("feature") {
    DependenciesFragment<FeatureComponent>()
}.init {
    app.component.featureComponent()
}