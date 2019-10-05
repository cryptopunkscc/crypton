package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.util.BaseActivity
import cc.cryptopunks.crypton.util.BaseFragment
import cc.cryptopunks.crypton.util.Scopes

data class ViewModelModule(
    private val featureComponent: FeatureComponent
) : ViewModelComponent,
    FeatureComponent by featureComponent {
    override val viewModelScope = Scopes.ViewModel(broadcastError)
}

fun BaseActivity.viewModelComponent() = ViewModelModule(
    featureComponent = featureComponent
)

fun BaseFragment.viewModelComponent() = baseActivity
    .viewModelComponent()