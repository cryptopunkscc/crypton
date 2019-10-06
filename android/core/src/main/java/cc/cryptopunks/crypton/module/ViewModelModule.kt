package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.activity.CoreActivity
import cc.cryptopunks.crypton.fragment.CoreFragment
import cc.cryptopunks.crypton.util.Scopes

data class ViewModelModule(
    private val featureComponent: FeatureComponent
) : ViewModelComponent,
    FeatureComponent by featureComponent {
    override val viewModelScope = Scopes.ViewModel(broadcastError)
}

fun CoreActivity.viewModelComponent() = ViewModelModule(
    featureComponent = featureComponent
)

fun CoreFragment.viewModelComponent() = baseActivity
    .viewModelComponent()